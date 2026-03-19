package chattingappbackend.models;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @Column(name = "notification_id")
    @JsonProperty("notification_id")
    private String notificationId;

    @Column(name = "user_id")
    @JsonProperty("user_id")
    private String userId;

    private String content;

    @Column(name = "is_read")
    @JsonProperty("is_read")
    private boolean isRead;

    @Column(name = "is_deleted")
    @JsonProperty("is_deleted")
    private boolean isDeleted;

    @Column(name = "created_at")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    // Constructor
    public Notification() {
    }

    public Notification(String notificationId, String userId, String content,
            boolean isRead, boolean isDeleted, LocalDateTime createdAt,
            NotificationType type) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.content = content;
        this.isRead = isRead;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.type = type;
    }

    // Getters & Setters
    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }
}
