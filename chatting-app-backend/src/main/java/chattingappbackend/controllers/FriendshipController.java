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
    
    //Tiêm các dịch vụ cần vào bean
    @Autowired
    FriendshipService friendshipService; //Gọi xử lý logic bạn bè

    @Autowired
    private JwtUtil jwtUtil; //Lấy user id từ JWT
    //Gửi lời mời kết bạn cần có email được truyền dưới dạng query param
    @PostMapping("/send")
    public ResponseEntity<ApiResponse<Void>> send(
            @RequestParam String email,
            HttpServletRequest request
    ) {
        //Lấy userID thông qua JWT UTIL
        String currentUserId = jwtUtil.getUserIdFromRequest(request);
        //Trả về thông báo từ service
        return ResponseEntity.ok(
                friendshipService.sendFriendRequest(currentUserId, email)
        );
    }
    //Chấp nhận lời mời kêt sbanj truyền id dưới dạng path variable
    @PostMapping("/accept/{id}")
    public ResponseEntity<ApiResponse<Void>> accept(
            @PathVariable String id,
            HttpServletRequest request
    ) {
        //Tìm thông tin người dùng
        String currentUserId = jwtUtil.getUserIdFromRequest(request);
        return ResponseEntity.ok(
                friendshipService.acceptRequest(id, currentUserId)
        );
    }
    //Lấy các lời mời ở trạng thái pending
    @GetMapping("/requests")
    public ResponseEntity<?> getPendingRequests(HttpServletRequest request) {

        String userId = jwtUtil.getUserIdFromRequest(request);

        return ResponseEntity.ok(
                friendshipService.getPendingRequests(userId)
        );
    }
    //Lấy danh sách bạn bè của user
    @GetMapping("/friends")
    public ResponseEntity<?> getFriends(HttpServletRequest request) {

        String userId = jwtUtil.getUserIdFromRequest(request);

        return ResponseEntity.ok(
                friendshipService.getFriends(userId)
        );
    }
    //Từ chối lời mời kết bạn
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
