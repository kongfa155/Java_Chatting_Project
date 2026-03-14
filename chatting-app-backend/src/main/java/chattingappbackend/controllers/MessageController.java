package chattingappbackend.controllers;
import chattingappbackend.dtos.SendMessageRequestDTO;
import chattingappbackend.exceptions.AppException;
import chattingappbackend.models.Message;
import chattingappbackend.responses.ApiResponse;
import chattingappbackend.services.JwtService;
import chattingappbackend.services.MessageService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<Message>> sendMessage(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody SendMessageRequestDTO request
    ) {

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new AppException("INVALID_TOKEN", "Token missing");
        }



        String token = authHeader.substring(7);
        String senderId = jwtService.extractUserId(token);

        
        ApiResponse<Message> response =
                messageService.sendMessage(senderId, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/conversation")
    public ResponseEntity<ApiResponse<List<Message>>> getConversation(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String userId
            
    ){

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new AppException("INVALID_TOKEN", "Token missing");
        }

        String token = authHeader.substring(7);
        String myId = jwtService.extractUserId(token);
          // 🔎 LOG DEBUG
        System.out.println("=== DEBUG CONVERSATION ===");
        System.out.println("Token userId (myId): " + myId);
        System.out.println("Query target userId: " + userId);
        System.out.println("==========================");
        ApiResponse<List<Message>> response =
                messageService.getConversation(myId, userId);

        return new ResponseEntity<>(response, HttpStatus.OK);
   
    }

}