package chattingapp.dtos;

import chattingapp.models.UserStatus;

import java.time.Instant;

public class UserDTO {
    private String userId;
    private Boolean gender;
    private String username;
    private String email;
    private String displayName;
    private String avatarUrl;
    private UserStatus status;
    private Instant createdAt;

    public UserDTO(String userId, Boolean gender, String username, String email, String displayName, String avatarUrl, Instant createdAt, UserStatus status) {
        this.userId = userId;
        this.gender = gender;
        this.username = username;
        this.email = email;
        this.displayName = displayName;
        this.avatarUrl = avatarUrl;
        this.createdAt = createdAt;
        this.status = status;
    }

    public UserDTO() {
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
