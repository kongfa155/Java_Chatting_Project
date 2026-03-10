package chattingappbackend.services;

import chattingappbackend.models.Message;
import chattingappbackend.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import chattingappbackend.dtos.SendMessageRequestDTO;
import java.util.UUID;

import java.time.LocalDateTime;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

   public Message sendMessage(String senderId, SendMessageRequestDTO request) {

    Message message = new Message();

    message.setMessageId(UUID.randomUUID().toString());
    message.setSenderId(senderId);
    message.setReceiverId(request.getReceiverId());
    message.setContent(request.getContent());
    message.setMessageType(request.getMessageType());
    message.setFileUrl(request.getFileUrl());

    message.setSentAt(LocalDateTime.now());
    message.setRead(false);
    message.setDeleted(false);

    return messageRepository.save(message);
}
}