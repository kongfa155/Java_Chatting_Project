/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chattingappbackend.models;

import java.time.LocalDateTime;

/**
 *
 * @author CP
 */
public class Message {

    //Properties
    private String messageId;
    private String senderId;
    private String receiverId;
    private String content;
    private boolean isRead;
    private LocalDateTime sentAt;
    private MessageType messageType;
    private boolean isDeleted;
    private String fileUrl;

    //Constructer
    public Message() {
    }

    public Message(String messageId, String senderId, String receiverId, String content,
            boolean isRead, LocalDateTime sentAt, MessageType messageType,
            boolean isDeleted, String fileUrl) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.isRead = isRead;
        this.sentAt = sentAt;
        this.messageType = messageType;
        this.isDeleted = isDeleted;
        this.fileUrl = fileUrl;
    }

    // Getters & Setters
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
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

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
