package chattingapp.dtos;

import chattingapp.models.UserStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class LoginResponseDTO {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private int expiresIn;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("username")
    private String username;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("gender")
    private boolean gender;
    @JsonProperty("status")
    private UserStatus status;
    @JsonProperty("created_at")
    private Instant createdAt;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String accessToken, int expiresIn, String userId, String username, String displayName, boolean gender, UserStatus status, Instant createdAt) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.userId = userId;
        this.username = username;
        this.displayName = displayName;
        this.gender = gender;
        this.status = status;
        this.createdAt = createdAt;
    }
}
