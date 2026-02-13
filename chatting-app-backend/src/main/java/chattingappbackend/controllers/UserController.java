package chattingappbackend.controllers;

import chattingappbackend.dtos.RegisterRequestDTO;
import chattingappbackend.dtos.RegisterResponseDTO;
import chattingappbackend.models.User;
import chattingappbackend.responses.ApiResponse;
import chattingappbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
