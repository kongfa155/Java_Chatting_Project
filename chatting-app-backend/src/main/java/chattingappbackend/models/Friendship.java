/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chattingappbackend.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author CP
 */
@Table("friendships")
public class Friendship {

    //Properties
    @Id
    @JsonProperty("friendship_id")
    private String friendshipId;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("friend_id")
    private String friendId;

    private FriendshipStatus status;
    
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    //Contruster
    public Friendship() {
    }

    public Friendship(String friendshipId, String userId, String friendId, FriendshipStatus status, LocalDateTime createdAt) {
        this.friendshipId = friendshipId;
        this.userId = userId;
        this.friendId = friendId;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters & Setters
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
