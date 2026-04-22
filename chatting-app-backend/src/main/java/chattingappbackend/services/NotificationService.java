package chattingappbackend.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import chattingappbackend.models.Notification;
import chattingappbackend.models.NotificationType;
import chattingappbackend.repositories.NotificationRepository;
import chattingappbackend.responses.ApiResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // tạo notification
    @Transactional
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

        // JPA dùng save()
        notificationRepository.save(notification);

        // Dùng Websocket để thông báo ngay lập tức
        messagingTemplate.convertAndSend(
                "/topic/notifications/" + userId,
                notification
        );
    }

    // lấy danh sách notification
    public ApiResponse<List<Notification>> getNotifications(String userId) {

        List<Notification> notifications
                = notificationRepository.findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(userId);

        return ApiResponse.success(
                "Get notifications successfully",
                notifications
        );
    }
}
