/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chattingapp.models;

/**
 *
 * @author CP
 */
public class ChatData {
    private String contactName;
    private String avatarUrl;
    private Message lastMessage; 
    private int unreadCount;
    public ChatData(String contactName, String avatarUrl, Message lastMessage, int unreadCount) {
        this.contactName = contactName;
        this.avatarUrl = avatarUrl;
        this.lastMessage = lastMessage;
        this.unreadCount = unreadCount;
    }

    public String getContactName() {
        return contactName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
    
}
