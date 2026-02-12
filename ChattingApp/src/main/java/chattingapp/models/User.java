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
public class User {

    private String userId;
    private Boolean gender;
    private String username;
    private String phoneNumber;
    private String displayName;
    private String avatarUrl;
    private String hashedPassword;
    private UserStatus status;
    private LocalDateTime createdAt;

    public User() {
    }

    public User(String userId, Boolean gender, String username, String phoneNumber,
                String displayName, String avatarUrl, String hashedPassword,
                UserStatus status, LocalDateTime createdAt) {
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
