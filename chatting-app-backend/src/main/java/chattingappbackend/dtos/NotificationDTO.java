package chattingappbackend.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import chattingappbackend.models.NotificationType;

public class NotificationDTO {

    @JsonProperty("notification_id")
    private String notificationId;

    private String content;

    private NotificationType type;

    @JsonProperty("is_read")
    private boolean isRead;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    public NotificationDTO() {
    }

    public NotificationDTO(String notificationId, String content,
            NotificationType type, boolean isRead,
            LocalDateTime createdAt) {
        this.notificationId = notificationId;
        this.content = content;
        this.type = type;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    
}
