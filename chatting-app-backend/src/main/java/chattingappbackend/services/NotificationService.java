package chattingappbackend.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chattingappbackend.models.Notification;
import chattingappbackend.models.NotificationType;
import chattingappbackend.repositories.NotificationRepository;
import chattingappbackend.responses.ApiResponse;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // tạo notification
    public void createNotification(String userId, String content, NotificationType type) {

        Notification notification = new Notification(
                UUID.randomUUID().toString(),
                userId,
                content,
                false,
                false,
                LocalDateTime.now(),
                type
        );

        notificationRepository.insertNotification(notification);
    }

    // lấy danh sách notification
    public ApiResponse<List<Notification>> getNotifications(String userId) {

        List<Notification> notifications
                = notificationRepository.findByUserId(userId);

        return ApiResponse.success(
                "Get notifications successfully",
                notifications
        );
    }

}
