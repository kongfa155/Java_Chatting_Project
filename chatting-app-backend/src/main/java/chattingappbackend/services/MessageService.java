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

import org.springframework.web.multipart.MultipartFile;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

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

        return ApiResponse.success("Message sent successfully", saved);
    }

    // GET CONVERSATION
    public ApiResponse<List<Message>> getConversation(String userA, String userB) {

            
    List<Message> messages =
            messageRepository.findConversation(userA, userB);

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


    public Message sendFile(String senderId, String receiverId, MultipartFile file) throws IOException {

            // tạo tên file random
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

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
            msg.setMessageType(MessageType.FILE);
            msg.setFileUrl("/uploads/" + fileName);
            msg.setSentAt(LocalDateTime.now());
            msg.setRead(false);
            msg.setDeleted(false);

            return messageRepository.save(msg);
        
    }
}