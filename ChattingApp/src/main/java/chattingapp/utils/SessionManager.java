package chattingapp.utils;

import chattingapp.models.User;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author CP
 */

//Lớp này được tạo để lưu thông tin người dùng sau khi login, giúp dễ truy xuất từ mọi file
public class SessionManager {

    private static String jwtToken;
    private static User currentUser;

    public static void setSession(String token, User user) {
        jwtToken = token;
        currentUser = user;
    }

    public static String getToken() {
        return jwtToken;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return jwtToken != null;
    }

    public static void clearSession() {
        jwtToken = null;
        currentUser = null;
    }

    public static String getUserId() {
        return currentUser != null ? currentUser.getUserId() : null;
    }
    public static void updateCurrentUser(User user) {
        if (currentUser != null) {
            currentUser.setDisplayName(user.getDisplayName());
            currentUser.setAvatarUrl(user.getAvatarUrl());
            currentUser.setGender(user.getGender());
            currentUser.setEmail(user.getEmail());
            // Cập nhật các trường cần thiết khác
        }
    }
}
