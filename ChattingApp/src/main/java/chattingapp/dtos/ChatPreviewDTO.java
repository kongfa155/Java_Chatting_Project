/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chattingapp.dtos;

import chattingapp.models.User;
import java.time.LocalDateTime;

/**
 *
 * @author CP
 */
public class ChatPreviewDTO {

    User user;
    String lastMessage;
    LocalDateTime lastTime;
    int unreadCount;

    public ChatPreviewDTO(User user, String lastMessage, LocalDateTime lastTime, int unreadCount) {
        this.user = user;
        this.lastMessage = lastMessage;
        this.lastTime = lastTime;
        this.unreadCount = unreadCount;
    }

    public User getUser() {
        return user;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public LocalDateTime getLastTime() {
        return lastTime;
    }

    public int getUnreadCount() {
        return unreadCount;
    }
}
