package chattingappbackend.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    //Tạo sẵn đối tượng truyền vào (inject)
    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    public ApiResponse<Void> sendFriendRequest(String senderId, String email) {
        User sender = userRepository.findById(senderId).orElseThrow(() -> new AppException("USER_NOT_FOUND", "Không tìm thấy người dùng"));
        User receiver = userRepository
                .findByEmail(email)
                .orElseThrow(()
                        -> new AppException("USER_NOT_FOUND", "Không tìm thấy người dùng"));

        if (receiver.getUserId().equals(senderId)) {
            throw new AppException("INVALID_ACTION", "Không thể tự kết bạn với chính mình");
        }

        Optional<Friendship> existing
                = friendshipRepository.findFriendshipBetween(senderId, receiver.getUserId());

        if (existing.isPresent()) {
            if (existing.get().getStatus() == FriendshipStatus.ACCEPTED) {
                throw new AppException("ALREADY_FRIENDS", "Already friends");
            }

            if (existing.get().getStatus() == FriendshipStatus.PENDING) {
                throw new AppException("REQUEST_ALREADY_SENT", "Friend request already sent");
            }

            if (existing.get().getStatus() == FriendshipStatus.DECLINED) {
                friendshipRepository.updateStatus(existing.get().getFriendshipId(), FriendshipStatus.PENDING);
                return ApiResponse.success("Friend request resent", null);
            }
        }

        Friendship friendship = new Friendship(
                UUID.randomUUID().toString(),
                senderId,
                receiver.getUserId(),
                FriendshipStatus.PENDING,
                LocalDateTime.now()
        );

        friendshipRepository.insertFriendship(friendship);

        //Tạo thông báo
        notificationService.createNotification(receiver.getUserId(), "Bạn có lời mời kết bạn mới đến từ " + sender.getDisplayName(), NotificationType.FRIEND);

        return ApiResponse.success("Friend request sent", null);
    }

    public ApiResponse<Void> acceptRequest(String friendshipId, String currentUserId) {

        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(()
                        -> new AppException("NOT_FOUND", "Friendship not found"));

        if (!friendship.getFriendId().equals(currentUserId)) {
            throw new AppException("FORBIDDEN", "You are not allowed");
        }

        friendshipRepository.updateStatus(
                friendshipId,
                FriendshipStatus.ACCEPTED
        );

        return ApiResponse.success("Friend request accepted", null);
    }

    public ApiResponse<Void> rejectRequest(String friendshipId, String currentUserId) {

        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(()
                        -> new AppException("NOT_FOUND", "Friendship not found"));

        if (!friendship.getFriendId().equals(currentUserId)) {
            throw new AppException("FORBIDDEN", "You are not allowed");
        }

        friendshipRepository.updateStatus(
                friendshipId,
                FriendshipStatus.DECLINED
        );

        return ApiResponse.success("Friend request reject", null);
    }

    public FriendshipStatus checkFriendship(String userA, String userB) {

        return friendshipRepository
                .findFriendshipBetween(userA, userB)
                .map(Friendship::getStatus)
                .orElse(null);
    }

    public ApiResponse<List<FriendRequestResponseDTO>> getPendingRequests(String userId) {

        var requests = friendshipRepository.findPendingRequests(userId);
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

    public ApiResponse<List<FriendRequestResponseDTO>> getFriends(String userId) {

        var friends = friendshipRepository.findFriend(userId);

        return ApiResponse.success(
                "Friend list fetched",
                friends
        );
    }
}
