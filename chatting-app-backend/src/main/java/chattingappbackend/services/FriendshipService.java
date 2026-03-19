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

@Service
public class FriendshipService {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public ApiResponse<Void> sendFriendRequest(String senderId, String email) {

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new AppException("USER_NOT_FOUND", "Không tìm thấy người dùng"));

        User receiver = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("USER_NOT_FOUND", "Không tìm thấy người dùng"));

        if (receiver.getUserId().equals(senderId)) {
            throw new AppException("INVALID_ACTION", "Không thể tự kết bạn với chính mình");
        }

        Optional<Friendship> existing
                = friendshipRepository.findFriendshipBetween(senderId, receiver.getUserId());

        if (existing.isPresent()) {
            Friendship f = existing.get();

            if (f.getStatus() == FriendshipStatus.ACCEPTED) {
                throw new AppException("ALREADY_FRIENDS", "Already friends");
            }

            if (f.getStatus() == FriendshipStatus.PENDING) {
                throw new AppException("REQUEST_ALREADY_SENT", "Friend request already sent");
            }

            if (f.getStatus() == FriendshipStatus.DECLINED) {
                // 👉 giữ style "update"
                f.setStatus(FriendshipStatus.PENDING);
                f.setCreatedAt(LocalDateTime.now());

                // ❗ KHÔNG cần save vẫn được (dirty checking)
                // nhưng để rõ ràng thì bạn có thể gọi:
                friendshipRepository.save(f);

                return ApiResponse.success("Friend request resent", null);
            }
        }

        // 👉 GIỮ CÁCH BẠN LÀM (tạo trong service)
        Friendship friendship = new Friendship(
                UUID.randomUUID().toString(), // ✔️ fix lỗi ID
                senderId,
                receiver.getUserId(),
                FriendshipStatus.PENDING,
                LocalDateTime.now()
        );

        friendshipRepository.save(friendship);

        // Notification
        notificationService.createNotification(
                receiver.getUserId(),
                "Bạn có lời mời kết bạn mới đến từ " + sender.getDisplayName(),
                NotificationType.FRIEND
        );

        return ApiResponse.success("Friend request sent", null);
    }

    // ========================
    // 2. ACCEPT REQUEST
    // ========================
    @Transactional
    public ApiResponse<Void> acceptRequest(String friendshipId, String currentUserId) {

        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new AppException("NOT_FOUND", "Friendship not found"));

        if (!friendship.getFriendId().equals(currentUserId)) {
            throw new AppException("FORBIDDEN", "You are not allowed");
        }

        // 👉 JPA dirty checking
        friendship.setStatus(FriendshipStatus.ACCEPTED);

        return ApiResponse.success("Friend request accepted", null);
    }

    // ========================
    // 3. REJECT REQUEST
    // ========================
    @Transactional
    public ApiResponse<Void> rejectRequest(String friendshipId, String currentUserId) {

        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new AppException("NOT_FOUND", "Friendship not found"));

        if (!friendship.getFriendId().equals(currentUserId)) {
            throw new AppException("FORBIDDEN", "You are not allowed");
        }

        // 👉 JPA dirty checking
        friendship.setStatus(FriendshipStatus.DECLINED);

        return ApiResponse.success("Friend request rejected", null);
    }

    // ========================
    // 4. CHECK FRIENDSHIP
    // ========================
    public FriendshipStatus checkFriendship(String userA, String userB) {

        return friendshipRepository
                .findFriendshipBetween(userA, userB)
                .map(Friendship::getStatus)
                .orElse(null);
    }

    // ========================
    // 5. GET PENDING REQUESTS
    // ========================
    public ApiResponse<List<FriendRequestResponseDTO>> getPendingRequests(String userId) {

        var requests = friendshipRepository.findPendingRequests(userId);

        // Debug
        System.out.println("==== DEBUG REQUESTS ====");
        for (FriendRequestResponseDTO r : requests) {
            System.out.println("Name: " + r.getDisplayName());
            System.out.println("UserId: " + r.getUserId());
        }

        return ApiResponse.success(
                "Pending friend requests fetched",
                requests
        );
    }

    // ========================
    // 6. GET FRIEND LIST
    // ========================
    public ApiResponse<List<FriendRequestResponseDTO>> getFriends(String userId) {

        var friends = friendshipRepository.findFriend(userId);

        return ApiResponse.success(
                "Friend list fetched",
                friends
        );
    }
}
