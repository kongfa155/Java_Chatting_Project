package chattingappbackend.controllers;

import chattingappbackend.models.Message;
import chattingappbackend.models.Notification;
import chattingappbackend.models.NotificationType;
import chattingappbackend.repositories.UserRepository;
import chattingappbackend.services.MessageService;
import chattingappbackend.services.NotificationService;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class ChatWebSocketController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private UserRepository userRepository;
    // Hashtable lưu trữ trạng thái các phòng chat đang hoạt động (nếu cần quản lý thêm)
    private final Map<String, String> activeChatRooms = new ConcurrentHashMap<>();

    @MessageMapping("/chat.send")
    public void sendMessage(Message message) {

        Message saved = messageService.saveFromSocket(message);

        String[] ids = {saved.getSenderId(), saved.getReceiverId()};
        Arrays.sort(ids);
        String chatId = ids[0] + "_" + ids[1];

        activeChatRooms.put(chatId, "ACTIVE");

        System.out.println("🚀 BROADCASTING TO ROOM: " + chatId);
        messagingTemplate.convertAndSend("/topic/chatroom/" + chatId, saved);

        // ✅ dùng service chuẩn
        notificationService.createNotification(
                saved.getReceiverId(),
                "Bạn có tin nhắn mới",
                NotificationType.MESSAGE
        );
    }
}
