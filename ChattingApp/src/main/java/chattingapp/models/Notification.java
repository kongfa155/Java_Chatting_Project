/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chattingapp.models;

import java.time.LocalDateTime;

/**
 *
 * @author CP
 */
public class Notification {
    //Properties
    private String notificationId;
    private String userId;
    private String content;
    private boolean isRead;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private NotificationType type;
    //Contruster
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
