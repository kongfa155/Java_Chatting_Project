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
    private User contact;
    private Message lastMessage; 
    private int unreadCount;
    
    public ChatData(User contact, Message lastMessage, int unreadCount) {
        this.contact = contact;
        this.lastMessage = lastMessage;
        this.unreadCount = unreadCount;
    }

    public User getContact() {
        return contact;
    }


    public Message getLastMessage() {
        return lastMessage;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setContact(User contact) {
        this.contact = contact;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
    
}
