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
public class NotificationManager {

    private static final List<Notification> notifications = new ArrayList<>();
    private static int unreadCount = 0;

    public static void add(Notification noti) {
        notifications.add(0, noti); // thêm lên đầu
        if (!noti.isRead()) {
            unreadCount++;
        }
    }

    public static List<Notification> getAll() {
        return notifications;
    }

    public static int getUnreadCount() {
        return unreadCount;
    }

    public static void markAllRead() {
        for (Notification n : notifications) {
            n.setRead(true);
        }
        unreadCount = 0;
    }
}
