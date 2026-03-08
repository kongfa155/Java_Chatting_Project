package chattingappbackend.controllers;

import chattingappbackend.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<ApiResponse<RegisterResponseDTO>> register(@RequestBody RegisterRequestDTO user){
        ApiResponse response= userService.register(user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PostMapping("/get-register-otp")
    public ResponseEntity<ApiResponse<Void>> sendRegisterOTP(@RequestBody RegisterOTPRequestDTO dto){

        ApiResponse<Void> response = userService.sendRegisterOTP(dto.getUsername());
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<RegisterVerifyResponseDTO>> verifyRegister(@RequestBody RegisterVerifyRequestDTO dto){
        ApiResponse response = userService.verifyRegister(dto);
        return new ResponseEntity<>(response,HttpStatus.OK );
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody LoginRequetDTO dto){
        ApiResponse response = userService.login(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
