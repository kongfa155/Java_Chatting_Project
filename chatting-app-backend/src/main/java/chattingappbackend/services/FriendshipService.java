package chattingappbackend.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import chattingappbackend.dtos.FriendRequestResponseDTO;
import chattingappbackend.exceptions.AppException;
import chattingappbackend.models.Friendship;
import chattingappbackend.models.FriendshipStatus;
import chattingappbackend.models.NotificationType;
import chattingappbackend.models.User;
import chattingappbackend.repositories.FriendshipRepository;
import chattingappbackend.repositories.UserRepository;
import chattingappbackend.responses.ApiResponse;

//Đây là lớp xử lý logic liên quan đến kết bạn
@Service
public class FriendshipService {

    //Nhờ JPA Inject các đối tượng cần vào lớp
    @Autowired
    private FriendshipRepository friendshipRepository; //Thao tác DB bảng Friendship

    @Autowired
    private UserRepository userRepository; // Thao tác db bản user (ở đây dùng để lấy thông tin user

    @Autowired
    private NotificationService notificationService; //Thao tác db bảng Noti, dùng để tạo thông báo
    //1. Gửi lời mời kết bạn

    @Transactional
    public ApiResponse<Void> sendFriendRequest(String senderId, String email) {

        //Tìm thông tin người gửi và người nhận, không thấy báo lỗi
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new AppException("USER_NOT_FOUND", "Không tìm thấy người dùng"));

        User receiver = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("USER_NOT_FOUND", "Không tìm thấy người dùng"));
        //Không cho phép kết bạn với chính mình
        if (receiver.getUserId().equals(senderId)) {
            throw new AppException("INVALID_ACTION", "Không thể tự kết bạn với chính mình");
        }
        //Tìm xem 2 người có bạn chưa (có thể trả về null nên xài optional)
        Optional<Friendship> existing
                = friendshipRepository.findFriendshipBetween(senderId, receiver.getUserId());
        //Nếu đã có, thông báo không cho kết bạn
        if (existing.isPresent()) {
            Friendship f = existing.get();

            if (f.getStatus() == FriendshipStatus.ACCEPTED) {
                throw new AppException("ALREADY_FRIENDS", "Already friends");
            }
            //Nếu chưa nhưng đã gửi lời mời thì thông báo là đã gửi
            if (f.getStatus() == FriendshipStatus.PENDING) {
                throw new AppException("REQUEST_ALREADY_SENT", "Friend request already sent");
            }
            //Nếu đã từng nhưng bị từ chối, cho phép kết bạn lại bằng cách cập nhật trạng thái về pending
            if (f.getStatus() == FriendshipStatus.DECLINED) {
                f.setStatus(FriendshipStatus.PENDING);
                f.setCreatedAt(LocalDateTime.now());

                // Khi tự close transaction, Hibernate thấy cập nhật dữ liệu, tự cập nhật
                // Nếu cần thiết thì có thể tự gọi
//                friendshipRepository.save(f);
                return ApiResponse.success("Friend request resent", null);
            }
        }

        // Tạo quan hệ trong csdl
        Friendship friendship = new Friendship(
                UUID.randomUUID().toString(), // Đánh ID ngẫu nhiên
                senderId,
                receiver.getUserId(),
                FriendshipStatus.PENDING,
                LocalDateTime.now()
        );
        //Lưu vào csdl
        friendshipRepository.save(friendship);

        // Tạo thông báo
        notificationService.createNotification(
                receiver.getUserId(),
                "Bạn có lời mời kết bạn mới đến từ " + sender.getDisplayName(),
                NotificationType.FRIEND
        );

        return ApiResponse.success("Friend request sent", null);
    }

    // 2. Chấp nhận lời mời kết bạn
    @Transactional
    public ApiResponse<Void> acceptRequest(String friendshipId, String currentUserId) {
        //Tìm yêu cầu kết bạn
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new AppException("NOT_FOUND", "Friendship not found"));
        //Kiểm tra quyền chấp nhận kết bạn, chỉ người được gửi mới được chấp nhận
        if (!friendship.getFriendId().equals(currentUserId)) {
            throw new AppException("FORBIDDEN", "You are not allowed");
        }

        // Cập nhật trạng thái, JPA tự cập nhật lại thông qua dirty Checking
        friendship.setStatus(FriendshipStatus.ACCEPTED);
        //Gửi thông báo đến người mời kết bạn
        notificationService.createNotification(
                friendship.getUserId(), // người gửi request ban đầu
                "Lời mời kết bạn đã được chấp nhận",
                NotificationType.FRIEND
        );
        return ApiResponse.success("Friend request accepted", null);
    }

    //3. Từ chối
    @Transactional
    public ApiResponse<Void> rejectRequest(String friendshipId, String currentUserId) {

        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new AppException("NOT_FOUND", "Friendship not found"));

        if (!friendship.getFriendId().equals(currentUserId)) {
            throw new AppException("FORBIDDEN", "You are not allowed");
        }

        // Dirty check tự cập nhật, không cần gọi save
        friendship.setStatus(FriendshipStatus.DECLINED);

        return ApiResponse.success("Friend request rejected", null);
    }

    // 4. Kiểm tra trạng thái bạn bè
    public FriendshipStatus checkFriendship(String userA, String userB) {
        //Gọi Repository để kiểm tra trạng thái hiện tại của bạn bè (nếu chưa từng có gì thì trả về null
        return friendshipRepository
                .findFriendshipBetween(userA, userB)
                .map(Friendship::getStatus)
                .orElse(null);
    }

    //5. Lấy tất cả lời mời kết bạn
    public ApiResponse<List<FriendRequestResponseDTO>> getPendingRequests(String userId) {
        //Nhận về DTO nên gọi var
        var requests = friendshipRepository.findPendingRequests(userId);

        // Debug
        System.out.println("==== DEBUG REQUESTS ====");
        for (FriendRequestResponseDTO r : requests) {
            System.out.println("Name: " + r.getDisplayName());
            System.out.println("UserId: " + r.getUserId());
        }
        //Lấy thành công
        return ApiResponse.success(
                "Pending friend requests fetched",
                requests
        );
    }

    //6. Lấy danh sách bạn bè
    public ApiResponse<List<FriendRequestResponseDTO>> getFriends(String userId) {

        var friends = friendshipRepository.findFriend(userId);

        return ApiResponse.success(
                "Friend list fetched",
                friends
        );
    }
    //7. Xóa bạn

    @Transactional
    public ApiResponse<Void> deleteFriend(String userA, String userB) {

        Optional<Friendship> existing
                = friendshipRepository.findFriendshipBetween(userA, userB);

        if (existing.isEmpty()) {
            throw new AppException("NOT_FOUND", "Friendship not found");
        }

        Friendship f = existing.get();

        // ❗ check trạng thái chi tiết
        if (f.getStatus() == FriendshipStatus.DECLINED) {
            throw new AppException("ALREADY_UNFRIENDED", "Already unfriended");
        }

        if (f.getStatus() == FriendshipStatus.PENDING) {
            throw new AppException("NOT_FRIEND", "Friend request not accepted yet");
        }

        // ✅ chuyển về DECLINED
        f.setStatus(FriendshipStatus.DECLINED);

        // (optional) cập nhật thời gian
        f.setCreatedAt(LocalDateTime.now());

        // (optional) gửi thông báo
        notificationService.createNotification(
                userB,
                "Bạn đã bị hủy kết bạn",
                NotificationType.FRIEND
        );

        return ApiResponse.success("Unfriended successfully", null);
    }
}
