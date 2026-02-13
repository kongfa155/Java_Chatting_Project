package chattingapp.dtos;

import chattingapp.models.UserStatus;

import java.time.LocalDateTime;

public class UserDTO {
    private String userId;
    private Boolean gender;
    private String username;
    private String phoneNumber;
    private String displayName;
    private String avatarUrl;
    private UserStatus status;
    private LocalDateTime createdAt;

    public UserDTO(String userId, Boolean gender, String username, String phoneNumber, String displayName, String avatarUrl, LocalDateTime createdAt, UserStatus status) {
        this.userId = userId;
        this.gender = gender;
        this.username = username;
        this.phoneNumber = phoneNumber;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
