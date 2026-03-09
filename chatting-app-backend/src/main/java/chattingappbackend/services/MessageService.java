package chattingappbackend.services;

import chattingappbackend.models.Message;
import chattingappbackend.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message sendMessage(Message message) {

        message.setSentAt(LocalDateTime.now());
        message.setRead(false);
        message.setDeleted(false);

        return messageRepository.save(message);
    }
}