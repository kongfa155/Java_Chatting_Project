package chattingappbackend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

// --- Annotation cho JDBC (Giữ cho bạn của bạn) ---
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

// --- Annotation cho JPA (Thêm mới cho bạn) ---
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

@Entity
@jakarta.persistence.Table(name = "users")
@Table(value = "users")
public class User {

    @Id
    @org.springframework.data.annotation.Id
    @jakarta.persistence.Column(name = "user_id")
    @JsonProperty("user_id")
    private String userId;

    @jakarta.persistence.Column(name = "gender")
    @JsonProperty("gender")
    private Boolean gender;

    @jakarta.persistence.Column(name = "username", unique = true, nullable = false)
    @JsonProperty("username")
    private String username;

    @jakarta.persistence.Column(name = "email", unique = true)
    @JsonProperty("email")
    private String email;

    @jakarta.persistence.Column(name = "display_name")
    @JsonProperty("display_name")
    private String displayName;

    @jakarta.persistence.Column(name = "avatar_url")
    @JsonProperty("avatar_url")
    private String avatarUrl;

    @jakarta.persistence.Column(name = "password")
    @Column("password")
    @JsonProperty("password")
    private String hashedPassword;

    @Enumerated(EnumType.STRING)
    @jakarta.persistence.Column(name = "status")
    @JsonProperty("status")
    private UserStatus status;

    @jakarta.persistence.Column(name = "created_at")
    @JsonProperty("created_at")
    private Instant createdAt;

    // --- CONSTRUCTORS ---
    public User() {
    }

    public User(String userId, Boolean gender, String username, String email, String displayName, String avatarUrl, String hashedPassword, UserStatus status, Instant createdAt) {
        this.userId = userId;
        this.gender = gender;
        this.username = username;
        this.email = email;
        this.displayName = displayName;
        this.avatarUrl = avatarUrl;
        this.hashedPassword = hashedPassword;
        this.status = status;
        this.createdAt = createdAt;
    }

    // --- GETTERS & SETTERS ---
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Boolean getGender() { return gender; }
    public void setGender(Boolean gender) { this.gender = gender; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getHashedPassword() { return hashedPassword; }
    public void setHashedPassword(String hashedPassword) { this.hashedPassword = hashedPassword; }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}