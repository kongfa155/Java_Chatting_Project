package chattingappbackend.services;

import chattingappbackend.dtos.RegisterRequestDTO;
import chattingappbackend.dtos.RegisterResponseDTO;
import chattingappbackend.exceptions.AppException;
import chattingappbackend.models.User;
import chattingappbackend.models.UserStatus;
import chattingappbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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



    public RegisterResponseDTO register(RegisterRequestDTO requestDTO){
        if(userRepository.findByUsername(requestDTO.getUsername()).isPresent()){
            throw new AppException("USERNAME_EXISTS","Input username have been used, please use another username.");
        }
        if(userRepository.existsByPhoneNumber(requestDTO.getPhoneNumber())){
            throw new AppException("PHONE_NUMBER_EXISTS", "Input phone number have been used, please use another phone number.");
        }
        User user = new User(
                UUID.randomUUID().toString(),
                requestDTO.getGender(),
                requestDTO.getUsername(),
                requestDTO.getPhoneNumber(),
                requestDTO.getDisplayName(),
                requestDTO.getAvatarUrl(),
                passwordEncoder.encode(requestDTO.getPassword()),
                UserStatus.UNVERIFIED,
                LocalDateTime.now()

        );

        userRepository.insertUser(user);
        return new RegisterResponseDTO(user.getDisplayName(), user.getStatus());
    }

}
