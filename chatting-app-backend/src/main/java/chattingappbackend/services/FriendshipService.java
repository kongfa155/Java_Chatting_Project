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
    //    Luồng xử lý:
    //    1. Tìm user bằng email
    //    2. Check thông tin hợp lý
    //    3. Reset trạng thái nếu đã bị từ chối
    //    4. Nếu chưa có tạo mới
    //    5. Gửi thông báo

    @Transactional
    public ApiResponse<Void> sendFriendRequest(String senderId, String email) {
        //Tìm user
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
                throw new AppException("ALREADY_FRIENDS", "Các bạn đã là bạn");
            }
            //Nếu chưa nhưng đã gửi lời mời thì thông báo là đã gửi
            if (f.getStatus() == FriendshipStatus.PENDING) {
                throw new AppException("REQUEST_ALREADY_SENT", "Lời mời kết bạn đã được gửi rồi");
            }
            //Nếu đã từng nhưng bị từ chối, cho phép kết bạn lại bằng cách cập nhật trạng thái về pending
            if (f.getStatus() == FriendshipStatus.DECLINED) {
                f.setStatus(FriendshipStatus.PENDING);
                f.setCreatedAt(LocalDateTime.now());

                // Khi tự close transaction, Hibernate thấy cập nhật dữ liệu, tự cập nhật
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
                "Bạn có lời mời kết bạn mới",
                NotificationType.FRIEND
        );

        return ApiResponse.success("Friend request sent", null);
    }

    // 2. Chấp nhận lời mời kết bạn
    @Transactional
    public ApiResponse<Void> acceptRequest(String friendshipId, String currentUserId) {
        //Tìm yêu cầu kết bạn
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new AppException("NOT_FOUND", "Không tìm thấy quan hệ bạn bè"));
        //Kiểm tra quyền chấp nhận kết bạn, chỉ người được gửi mới được chấp nhận
        if (!friendship.getFriendId().equals(currentUserId)) {
            throw new AppException("FORBIDDEN", "Bạn không được phép kết bạn với chính mình");
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
                .orElseThrow(() -> new AppException("NOT_FOUND", "Không tìm thấy bạn"));

        if (!friendship.getFriendId().equals(currentUserId)) {
            throw new AppException("FORBIDDEN", "Bạn không được phép thực hiện hành vi này với chính mình");
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
        //  System.out.println("==== DEBUG REQUESTS ====");
        //        for (FriendRequestResponseDTO r : requests) {
        //            System.out.println("Name: " + r.getDisplayName());
        //            System.out.println("UserId: " + r.getUserId());
        //        }
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
        //Kiểm tra trạng thái bạn
        Optional<Friendship> existing
                = friendshipRepository.findFriendshipBetween(userA, userB);

        if (existing.isEmpty()) {
            throw new AppException("NOT_FOUND", "Không tìm thấy bạn");
        }

        Friendship f = existing.get();

        // Check trạng thái chi tiết
        if (f.getStatus() == FriendshipStatus.DECLINED) {
            throw new AppException("ALREADY_UNFRIENDED", "Đã xóa kết bạn sẵn");
        }

        if (f.getStatus() == FriendshipStatus.PENDING) {
            throw new AppException("NOT_FRIEND", "Lời mời kết bạn chưa được chấp nhận");
        }

        // chuyển về DECLINED
        f.setStatus(FriendshipStatus.DECLINED);

        // cập nhật thời gian
        f.setCreatedAt(LocalDateTime.now());

        return ApiResponse.success("Unfriended successfully", null);
    }
}
