
package chattingappbackend.controllers;

import chattingappbackend.models.Message;
import chattingappbackend.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    @Autowired
    private MessageService messageService;

    @MessageMapping("/chat.send")
    public void sendMessage(Message message) {
        // lưu DB
        messageService.saveFromSocket(message);
    }
}