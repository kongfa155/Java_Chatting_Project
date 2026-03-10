
package chattingappbackend.controllers;

import chattingappbackend.models.Message;
import chattingappbackend.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import chattingappbackend.dtos.SendMessageRequestDTO;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

  @PostMapping
public Message sendMessage(@RequestBody SendMessageRequestDTO request) {

    String senderId = "test-user"; // tạm thời

    return messageService.sendMessage(senderId, request);
}
}