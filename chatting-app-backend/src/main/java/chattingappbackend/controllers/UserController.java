package chattingappbackend.controllers;

import chattingappbackend.dtos.*;
import chattingappbackend.exceptions.AppException;
import chattingappbackend.models.User;
import chattingappbackend.dtos.SearchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import chattingappbackend.responses.ApiResponse;
import chattingappbackend.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponseDTO>> register(@RequestBody RegisterRequestDTO user) {
        ApiResponse response = userService.register(user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/get-register-otp")
    public ResponseEntity<ApiResponse<Void>> sendRegisterOTP(@RequestBody RegisterOTPRequestDTO dto) {

        ApiResponse<Void> response = userService.sendRegisterOTP(dto.getUsername());
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<RegisterVerifyResponseDTO>> verifyRegister(@RequestBody RegisterVerifyRequestDTO dto) {
        ApiResponse response = userService.verifyRegister(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody LoginRequetDTO dto) {
        ApiResponse response = userService.login(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@RequestHeader("Authorization") String authHeader, @RequestBody ChangePasswordRequestDTO dto) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AppException("INVALID_TOKEN", "Your token is outdated or missing.");
        }
        String token = authHeader.substring(7);
        ApiResponse response = userService.changePassword(token, dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> searchUser(
            @RequestParam String email) {

        ApiResponse<?> response = userService.findByEmail(email);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
