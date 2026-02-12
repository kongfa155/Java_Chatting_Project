package chattingappbackend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(value ="users")
public class User {
    @Id
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("gender")
    private Boolean gender;
    @JsonProperty("username")
    private String username;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("avatar_url")
    private String avatarUrl;
    @Column("password")
    @JsonProperty("password")
    private String hashedPassword;
    @JsonProperty("status")
    private UserStatus status;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    public User(String userId, Boolean gender, String username, String phoneNumber, String displayName, String avatarUrl, String hashedPassword, UserStatus status, LocalDateTime createdAt) {
        this.userId = userId;
        this.gender = gender;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.displayName = displayName;
        this.avatarUrl = avatarUrl;
        this.hashedPassword = hashedPassword;
        this.status = status;
        this.createdAt = createdAt;
    }

    public User() {
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

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
