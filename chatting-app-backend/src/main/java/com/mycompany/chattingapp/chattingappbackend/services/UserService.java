package com.mycompany.chattingapp.chattingappbackend.services;

import com.mycompany.chattingapp.chattingappbackend.dtos.RegisterResponseDTO;
import com.mycompany.chattingapp.chattingappbackend.dtos.UserDTO;
import com.mycompany.chattingapp.chattingappbackend.models.User;
import com.mycompany.chattingapp.chattingappbackend.models.UserStatus;
import com.mycompany.chattingapp.chattingappbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {
    private UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public RegisterResponseDTO register(User userModel){
        if(userRepository.findByUsername(userModel.getUsername()).isPresent()){
            throw new RuntimeException("USERNAME_EXISTS");
        }
        if(userRepository.existsByPhoneNumber(userModel.getPhoneNumber())){
            throw new RuntimeException("PHONE_NUMBER_EXISTS");
        }
        userModel.setUserId(UUID.randomUUID().toString());
        userModel.setHashedPassword(passwordEncoder.encode(userModel.getHashedPassword()));
        userModel.setCreatedAt(LocalDateTime.now());
        userModel.setStatus(UserStatus.UNVERIFIED);

        userRepository.insertUser(userModel);
        return new RegisterResponseDTO(userModel.getDisplayName(), userModel.getStatus());
    }

}
