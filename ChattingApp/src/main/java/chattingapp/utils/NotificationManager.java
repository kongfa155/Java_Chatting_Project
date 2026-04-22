/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chattingapp.utils;

import chattingapp.models.Notification;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author CP
 */

//Quản lý notificaiton ở FE, dùng để hạn chế load lại
public class NotificationManager {

    private static final List<Notification> notifications = new ArrayList<>();

    public static List<Notification> getAll() {
        return notifications;
    }

    public static void add(Notification noti) {
        notifications.add(0, noti);
    }

    public static int getUnreadCount() {
        // Tính toán dựa trên trạng thái thực tế của list
        return (int) notifications.stream().filter(n -> !n.isRead()).count();
    }

    public static void markAllRead() {
        for (Notification n : notifications) {
            n.setRead(true);
        }
    }
}
