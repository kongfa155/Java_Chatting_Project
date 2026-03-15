package chattingappbackend.controllers;

import chattingappbackend.dtos.SearchDTO;
import chattingappbackend.dtos.user.changeemail.ChangeEmailOTPRequestDTO;
import chattingappbackend.dtos.user.changeemail.ChangeEmailRequestDTO;
import chattingappbackend.dtos.user.changepassword.ChangePasswordRequestDTO;
import chattingappbackend.dtos.user.changeprofile.ChangeProfileRequestDTO;
import chattingappbackend.dtos.user.changeprofile.ChangeProfileResponseDTO;
import chattingappbackend.dtos.user.forgotpassword.ForgotPasswordOTPRequestDTO;
import chattingappbackend.dtos.user.forgotpassword.ForgotPasswordVerifyRequest;
import chattingappbackend.dtos.user.login.LoginRequetDTO;
import chattingappbackend.dtos.user.login.LoginResponseDTO;
import chattingappbackend.dtos.user.register.*;
import chattingappbackend.exceptions.AppException;
import chattingappbackend.utils.JwtUtil;
import jakarta.validation.Valid;
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
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponseDTO>> register(@Valid @RequestBody RegisterRequestDTO user) {
        ApiResponse<RegisterResponseDTO> response = userService.register(user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/get-register-otp")
    public ResponseEntity<ApiResponse<Void>> sendRegisterOTP(@Valid @RequestBody RegisterOTPRequestDTO dto) {
        ApiResponse<Void> response = userService.sendRegisterOTP(dto.username());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<RegisterVerifyResponseDTO>> verifyRegister(@Valid @RequestBody RegisterVerifyRequestDTO dto) {
        ApiResponse<RegisterVerifyResponseDTO> response = userService.verifyRegister(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/change-email")
    public ResponseEntity<ApiResponse<Void>> changeEmailOTP(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody ChangeEmailOTPRequestDTO dto){
        String userId = jwtUtil.getUserIdFromJwt(authHeader);
        ApiResponse<Void> response = userService.getChangeEmailOTP(userId, dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/change-email")
    public ResponseEntity<ApiResponse<Void>> changeEmail(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody ChangeEmailRequestDTO dto){
        String userId = jwtUtil.getUserIdFromJwt(authHeader);
        ApiResponse<Void> response = userService.changeEmail(userId, dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginRequetDTO dto) {
        ApiResponse<LoginResponseDTO> response = userService.login(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody ChangePasswordRequestDTO dto) {
        String userId = jwtUtil.getUserIdFromJwt(authHeader);
        ApiResponse<Void> response = userService.changePassword(userId, dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<SearchDTO>> searchUser(@RequestParam String email) {
        ApiResponse<SearchDTO> response = userService.findByEmail(email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/change-profile")
    public ResponseEntity<ApiResponse<ChangeProfileResponseDTO>> changeProfile(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody ChangeProfileRequestDTO dto){
        String userId = jwtUtil.getUserIdFromJwt(authHeader);
        ApiResponse<ChangeProfileResponseDTO> response = userService.changeProfile(userId, dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPasswordOTP(@Valid @RequestBody ForgotPasswordOTPRequestDTO dto){
        ApiResponse<Void> response = userService.forgotPasswordOTP(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPasswordVerify(@Valid @RequestBody ForgotPasswordVerifyRequest dto){
        ApiResponse<Void> response = userService.forgotPasswordVerify(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}