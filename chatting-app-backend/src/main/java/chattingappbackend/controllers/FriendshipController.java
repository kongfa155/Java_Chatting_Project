package chattingappbackend.controllers;

import chattingappbackend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import chattingappbackend.responses.ApiResponse;
import chattingappbackend.services.FriendshipService;
import chattingappbackend.services.UserService;
import chattingappbackend.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/friendships")
public class FriendshipController {

    @Autowired
    FriendshipService friendshipService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<Void>> send(
            @RequestParam String email,
            HttpServletRequest request
    ) {
        String currentUserId = jwtUtil.getUserIdFromRequest(request);

        return ResponseEntity.ok(
                friendshipService.sendFriendRequest(currentUserId, email)
        );
    }

    @PostMapping("/accept/{id}")
    public ResponseEntity<ApiResponse<Void>> accept(
            @PathVariable String id,
            HttpServletRequest request
    ) {
        String currentUserId = jwtUtil.getUserIdFromRequest(request);
        return ResponseEntity.ok(
                friendshipService.acceptRequest(id, currentUserId)
        );
    }

    @GetMapping("/requests")
    public ResponseEntity<?> getPendingRequests(HttpServletRequest request) {

        String userId = jwtUtil.getUserIdFromRequest(request);

        return ResponseEntity.ok(
                friendshipService.getPendingRequests(userId)
        );
    }

    @GetMapping("/friends")
    public ResponseEntity<?> getFriends(HttpServletRequest request) {

        String userId = jwtUtil.getUserIdFromRequest(request);

        return ResponseEntity.ok(
                friendshipService.getFriends(userId)
        );
    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<ApiResponse<Void>> reject(
            @PathVariable String id,
            HttpServletRequest request
    ) {
        String currentUserId = jwtUtil.getUserIdFromRequest(request);
        return ResponseEntity.ok(
                friendshipService.rejectRequest(id, currentUserId)
        );
    }
}
