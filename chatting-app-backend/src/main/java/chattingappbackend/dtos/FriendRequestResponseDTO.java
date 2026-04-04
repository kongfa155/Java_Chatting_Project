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

    public String getFriendshipId() {
        return friendshipId;
    }

    public void setFriendshipId(String friendshipId) {
        this.friendshipId = friendshipId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public FriendshipStatus getStatus() {
        return status;
    }

    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }

    public FriendRequestResponseDTO() {}
}