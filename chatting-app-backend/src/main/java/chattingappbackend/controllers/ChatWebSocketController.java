package chattingappbackend.controllers;

import chattingappbackend.models.Message;
import chattingappbackend.models.NotificationType;
import chattingappbackend.repositories.UserRepository;
import chattingappbackend.services.MessageService;
import chattingappbackend.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class ChatWebSocketController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    // Hashtable lưu trữ trạng thái các phòng chat đang hoạt động (nếu cần quản lý thêm)
    private final Map<String, String> activeChatRooms = new ConcurrentHashMap<>();

    @MessageMapping("/chat.send")
    public void sendMessage(Message message) {
        //Lưu tin nhắn vào hệ thống
        Message saved = messageService.saveFromSocket(message);
        //Tạo chat room ID động (2 người A và B) luôn lắng nghe chat trên cùng 1 kênh ws
        String[] ids = {saved.getSenderId(), saved.getReceiverId()};
        Arrays.sort(ids);
        String chatId = ids[0] + "_" + ids[1];

        activeChatRooms.put(chatId, "ACTIVE");

//        System.out.println("Lắng nghe đến room: " + chatId);
        //Chuyển object thành tin nhắn gửi đến đích dạng STOMP của ws. 2 tham số destination (nơi gửi đến) và payload (nội dung cần gửi)
        messagingTemplate.convertAndSend("/topic/chatroom/" + chatId, saved);

        // Tạo thông báo 
        notificationService.createNotification(
                saved.getReceiverId(),
                "Bạn có tin nhắn mới",
                NotificationType.MESSAGE
        );
    }
}
