package chattingappbackend.controllers;

import chattingappbackend.models.Message;
import chattingappbackend.models.Notification;
import chattingappbackend.repositories.UserRepository;
import chattingappbackend.services.MessageService;
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
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private UserRepository userRepository;
    // Hashtable lưu trữ trạng thái các phòng chat đang hoạt động (nếu cần quản lý thêm)
    private final Map<String, String> activeChatRooms = new ConcurrentHashMap<>();

    @MessageMapping("/chat.send")
    public void sendMessage(Message message) {
        // 1. Gọi service cũ của bạn để lưu DB (giữ nguyên logic của bạn)
        messageService.saveFromSocket(message);

        // 2. Tạo chatId duy nhất dựa trên sender và receiver
        String[] ids = {message.getSenderId(), message.getReceiverId()};
        Arrays.sort(ids);
        String chatId = ids[0] + "_" + ids[1];

        // Lưu vào map để đánh dấu phòng này đang có hoạt động
        activeChatRooms.put(chatId, "ACTIVE");

        // 3. Gửi tin nhắn đến kênh CHUNG của 2 người
        // Thay vì gửi đến /topic/messages/{id}, ta gửi đến /topic/chatroom/{chatId}
        System.out.println("🚀 BROADCASTING TO ROOM: " + chatId);
        messagingTemplate.convertAndSend("/topic/chatroom/" + chatId, message);
// Lấy thông tin người gửi để lấy Display Name
        String senderDisplayName = userRepository.findById(message.getSenderId())
                .map(user -> user.getDisplayName())
                .orElse("Người dùng lạ"); // Trường hợp dự phòng nếu không tìm thấy User
        // Gửi notification cho người nhận
        messagingTemplate.convertAndSend(
                "/topic/notifications/" + message.getReceiverId(),
                new Notification(
                        UUID.randomUUID().toString(),
                        message.getReceiverId(),
                        "Bạn có tin nhắn mới từ " + senderDisplayName,
                        false,
                        false,
                        LocalDateTime.now(),
                        null
                )
        );
    }
}
