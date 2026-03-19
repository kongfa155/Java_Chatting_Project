package chattingappbackend.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "friendships")
public class Friendship {

    @Id
    @Column(name = "friendship_id")
    private String friendshipId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "friend_id")
    private String friendId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private FriendshipStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Friendship() {
    }

    public Friendship(String friendshipId, String userId, String friendId, FriendshipStatus status, LocalDateTime createdAt) {
        this.friendshipId = friendshipId;
        this.userId = userId;
        this.friendId = friendId;
        this.status = status;
        this.createdAt = createdAt;
    }

    // getters & setters

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

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public FriendshipStatus getStatus() {
        return status;
    }

    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
}
