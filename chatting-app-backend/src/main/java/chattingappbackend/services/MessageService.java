package chattingappbackend.services;

import chattingappbackend.models.Message;
import chattingappbackend.models.MessageType;
import chattingappbackend.dtos.SendMessageRequestDTO;
import chattingappbackend.exceptions.AppException;
import chattingappbackend.repositories.MessageRepository;
import chattingappbackend.responses.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.web.multipart.MultipartFile;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // SEND MESSAGE
    public ApiResponse<Message> sendMessage(String senderId, SendMessageRequestDTO request) {

        if (request.getReceiverId() == null || request.getReceiverId().isBlank()) {
            throw new AppException("INVALID_RECEIVER", "Receiver id is required");
        }

        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new AppException("EMPTY_MESSAGE", "Message content cannot be empty");
        }

        Message message = new Message();

        message.setMessageId(UUID.randomUUID().toString());
        message.setSenderId(senderId);
        message.setReceiverId(request.getReceiverId());
        message.setContent(request.getContent());
        message.setMessageType(MessageType.TEXT);
        message.setSentAt(LocalDateTime.now());
        message.setRead(false);
        message.setDeleted(false);

        Message saved = messageRepository.save(message);

        // 🚀 push cho receiver
        messagingTemplate.convertAndSend(
                "/topic/messages/" + saved.getReceiverId(),
                saved
        );

        // 🚀 push cho chính sender
        messagingTemplate.convertAndSend(
                "/topic/messages/" + saved.getSenderId(),
                saved
        );

        return ApiResponse.success("Message sent successfully", saved);
    }

    public void saveFromSocket(Message message) {

        message.setMessageId(UUID.randomUUID().toString());
        message.setSentAt(LocalDateTime.now());
        message.setRead(false);
        message.setDeleted(false);

        Message saved = messageRepository.save(message);

        // 🚀 push realtime cho người nhận
        // 🚀 receiver
        messagingTemplate.convertAndSend(
                "/topic/messages/" + saved.getReceiverId(),
                saved
        );

        // 🚀 sender (để UI nó cũng nhận realtime)
        messagingTemplate.convertAndSend(
                "/topic/messages/" + saved.getSenderId(),
                saved
        );
        //Thông báo
        messagingTemplate.convertAndSend(
                "/topic/notifications/" + saved.getReceiverId(),
                "Bạn có tin nhắn mới"
        );
    }

    // GET CONVERSATION
    public ApiResponse<List<Message>> getConversation(String userA, String userB) {

        List<Message> messages
                = messageRepository.findConversation(userA, userB);

        // bỏ message đã delete
        messages.removeIf(Message::isDeleted);

        return ApiResponse.success("Conversation fetched successfully", messages);
    }

    // MARK MESSAGE AS READ
    public ApiResponse<Void> markRead(String messageId) {

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new AppException("MESSAGE_NOT_FOUND", "Message not found"));

        message.setRead(true);

        messageRepository.save(message);

        return ApiResponse.success("Message marked as read", null);
    }

    // DELETE MESSAGE (SOFT DELETE)
    public ApiResponse<Void> deleteMessage(String messageId) {

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new AppException("MESSAGE_NOT_FOUND", "Message not found"));

        message.setDeleted(true);

        messageRepository.save(message);

        return ApiResponse.success("Message deleted", null);
    }
    //SEND FILE

    private MessageType detectFileType(String ext) {

        String name = ext.toLowerCase();

        if (name.equals(".png") || name.equals(".jpg") || name.equals(".jpeg")) {
            return MessageType.IMAGE;
        }

        if (name.equals(".mp4")) {
            return MessageType.VIDEO;
        }

        if (name.equals(".mp3") || name.equals(".wav")) {
            return MessageType.AUDIO;
        }

        if (name.equals(".pdf")
                || name.equals(".docx")
                || name.equals(".xlsx")
                || name.equals(".pptx")) {
            return MessageType.FILE;
        }

        return MessageType.FILE;
    }

    public Message sendFile(String senderId, String receiverId, MultipartFile file) throws IOException {

        // tạo tên file random
        String originalName = file.getOriginalFilename();
        String extension = "";

        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }

        // filename sạch (không dính tiếng Việt)
        String fileName = UUID.randomUUID() + extension;

        // tạo folder uploads nếu chưa có
        Path uploadDir = Paths.get("uploads");
        Files.createDirectories(uploadDir);

        // đường dẫn file
        Path filePath = uploadDir.resolve(fileName);

        // lưu file
        Files.copy(file.getInputStream(), filePath);

        // tạo message
        Message msg = new Message();
        msg.setMessageId(UUID.randomUUID().toString());
        msg.setSenderId(senderId);
        msg.setReceiverId(receiverId);
        // set type đúng
        msg.setMessageType(detectFileType(extension));

        // lưu tên file để FE hiển thị
        msg.setContent(file.getOriginalFilename());

        // url file
        msg.setFileUrl("/uploads/" + fileName);
        msg.setSentAt(LocalDateTime.now());
        msg.setRead(false);
        msg.setDeleted(false);

        Message saved = messageRepository.save(msg);

        // 🚀 push realtime
        messagingTemplate.convertAndSend(
                "/topic/messages/" + receiverId,
                saved
        );

        messagingTemplate.convertAndSend(
                "/topic/messages/" + senderId,
                saved
        );

        return saved;

    }
}
