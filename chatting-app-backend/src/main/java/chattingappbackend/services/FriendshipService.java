package chattingappbackend.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chattingappbackend.exceptions.AppException;
import chattingappbackend.models.Friendship;
import chattingappbackend.models.FriendshipStatus;
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

    public ApiResponse<Void> sendFriendRequest(String senderId, String phoneNumber) {

        User receiver = userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(()
                        -> new AppException("USER_NOT_FOUND", "User not found"));

        if (receiver.getUserId().equals(senderId)) {
            throw new AppException("INVALID_ACTION", "Cannot add yourself");
        }

        Optional<Friendship> existing
                = friendshipRepository.findFriendshipBetween(senderId, receiver.getUserId());

        if (existing.isPresent()) {
            throw new AppException("FRIENDSHIP_EXISTS", "Friendship already exists");
        }

        Friendship friendship = new Friendship(
                UUID.randomUUID().toString(),
                senderId,
                receiver.getUserId(),
                FriendshipStatus.PENDING,
                LocalDateTime.now()
        );

        friendshipRepository.insertFriendship(friendship);

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

    public FriendshipStatus checkFriendship(String userA, String userB) {

        return friendshipRepository
                .findFriendshipBetween(userA, userB)
                .map(Friendship::getStatus)
                .orElse(null);
    }
}
