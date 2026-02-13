package chattingappbackend.controllers;

import chattingappbackend.dtos.*;
import chattingappbackend.responses.ApiResponse;
import chattingappbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponseDTO>> register(@RequestBody RegisterRequestDTO user){
        RegisterResponseDTO result = userService.register(user);
        ApiResponse<RegisterResponseDTO> response = ApiResponse.success("User registered successfully", result);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PostMapping("/get-register-otp")
    public ResponseEntity<ApiResponse<Void>> sendRegisterOTP(@RequestBody RegisterOTPRequestDTO dto){

        ApiResponse<Void> response = userService.sendRegisterOTP(dto.getUsername());
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<RegisterVerifyResponseDTO>> verifyRegister(@RequestBody RegisterVerifyRequestDTO dto){
        RegisterVerifyResponseDTO result = userService.verifyRegister(dto);
        ApiResponse<RegisterVerifyResponseDTO> response = ApiResponse.success("Account verified successfully", result);
        return new ResponseEntity<>(response,HttpStatus.CREATED );
    }

}
