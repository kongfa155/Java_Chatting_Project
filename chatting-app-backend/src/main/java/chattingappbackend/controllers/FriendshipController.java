package chattingappbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import chattingappbackend.responses.ApiResponse;
import chattingappbackend.services.FriendshipService;

@RestController
@RequestMapping("/api/friendships")
public class FriendshipController {

    @Autowired
    FriendshipService friendshipService;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<Void>> send(
            @RequestParam String email,
            @RequestParam String currentUserId
    ) {
        return ResponseEntity.ok(
                friendshipService.sendFriendRequest(currentUserId, email)
        );
    }

    @PostMapping("/accept/{id}")
    public ResponseEntity<ApiResponse<Void>> accept(
            @PathVariable String id,
            @RequestParam String currentUserId
    ) {
        return ResponseEntity.ok(
                friendshipService.acceptRequest(id, currentUserId)
        );
    }
}
