package chattingappbackend.dtos;

import chattingappbackend.models.FriendshipStatus;

public class FriendRequestResponseDTO {

    private String friendshipId;
    private String userId;
    private String displayName;
    private String avatarUrl;
    private FriendshipStatus status;

    public FriendRequestResponseDTO(
            String friendshipId,
            String userId,
            String displayName,
            String avatarUrl,
            FriendshipStatus status) {

        this.friendshipId = friendshipId;
        this.userId = userId;
        this.displayName = displayName;
        this.avatarUrl = avatarUrl;
        this.status = status;
    }

    public FriendRequestResponseDTO() {
    }
}
