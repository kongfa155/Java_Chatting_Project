package chattingappbackend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chattingappbackend.models.Notification;
import chattingappbackend.responses.ApiResponse;
import chattingappbackend.services.NotificationService;
import chattingappbackend.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private JwtUtil jwtUtil;

    // lấy danh sách thông báo
    @GetMapping("/{userId}")
    public ApiResponse<List<Notification>> getNotifications(
            HttpServletRequest request
    ) {
        String userId = jwtUtil.getUserIdFromRequest(request);
        return notificationService.getNotifications(userId);
    }

}
