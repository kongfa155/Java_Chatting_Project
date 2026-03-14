package chattingappbackend.services;

import chattingappbackend.models.Message;
import chattingappbackend.models.MessageType;
import chattingappbackend.dtos.SendMessageRequestDTO;
import chattingappbackend.exceptions.AppException;
import chattingappbackend.repositories.MessageRepository;
import chattingappbackend.responses.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

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
                messageRepository.findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(
                        userA, userB,
                        userB, userA
                );

        // bỏ message đã delete
        messages.removeIf(Message::isDeleted);

        // sort theo thời gian
        messages.sort(Comparator.comparing(Message::getSentAt));

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
}