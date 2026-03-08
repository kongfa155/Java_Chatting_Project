package chattingappbackend.services;

import chattingappbackend.dtos.*;
import chattingappbackend.exceptions.AppException;
import chattingappbackend.external_services.otp_services.OTPService;
import chattingappbackend.models.User;
import chattingappbackend.models.UserStatus;
import chattingappbackend.repositories.UserRepository;
import chattingappbackend.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {
    private UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private OTPService otpService;
    private JwtService jwtService;

    public OTPService getOtpService() {
        return otpService;
    }
    @Autowired
    public void setOtpService(OTPService otpService) {
        this.otpService = otpService;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    public ApiResponse<RegisterResponseDTO> register(RegisterRequestDTO requestDTO){
        if(userRepository.findByUsername(requestDTO.getUsername()).isPresent()){
            throw new AppException("USERNAME_EXISTS","Input username have been used, please use another username.");
        }
        if(userRepository.existsByEmail(requestDTO.getEmail())){
            throw new AppException("EMAIL_EXISTS", "Input email have been used, please use another email.");
        }
        User user = new User(
                UUID.randomUUID().toString(),
                requestDTO.getGender(),
                requestDTO.getUsername(),
                requestDTO.getEmail(),
                requestDTO.getDisplayName(),
                requestDTO.getAvatarUrl(),
                passwordEncoder.encode(requestDTO.getPassword()),
                UserStatus.UNVERIFIED,
                Instant.now()

        );

        userRepository.insertUser(user);
        return ApiResponse.success("User registered successfully",new RegisterResponseDTO(user.getDisplayName(), user.getStatus()) );
    }
    public ApiResponse<Void> sendRegisterOTP(String username){
        if(userRepository.findByUsername(username).isEmpty()){
            throw new AppException("INVALID_USERNAME", "Input username is invalid.");
        }
        UserStatus currentStatus = userRepository.findStatusByUsername(username)
                .orElse(null);
        if(currentStatus!=UserStatus.UNVERIFIED){
            throw new AppException("ACCOUNT_ALREADY_VERIFIED", "Account have already been verified");
        }
        String email = userRepository.findEmailByUsername(username).orElse(null);
        boolean sent = otpService.sendOTP(email);
        if(sent) {
            return ApiResponse.success("Sent OTP successfully", null);
        }   else{
            throw new AppException("SEND_OTP_FAILED", "Could not send OTP");
        }
    }
    public ApiResponse<RegisterVerifyResponseDTO> verifyRegister(RegisterVerifyRequestDTO requestDTO){
        UserStatus userStatus = userRepository.findStatusByUsername(requestDTO.getUsername()).orElseThrow(()->new AppException("INVALID_USERNAME","Input username is invalid."));
        if(userStatus!=UserStatus.UNVERIFIED){
            throw new AppException("ACCOUNT_ALREADY_VERIFIED","Account have already been verified");
        }
        String email =  userRepository.findEmailByUsername(requestDTO.getUsername()).orElse(null);
        boolean verify = otpService.checkOTP(requestDTO.getOtp(), email);
        if(verify){
            userRepository.updateStatusByUsername(requestDTO.getUsername(), UserStatus.ACTIVATED);
            RegisterVerifyResponseDTO response = userRepository.findUserForVerification(requestDTO.getUsername()).orElseThrow(()-> new AppException("INVALID_USERNAME","Input username is invalid."));
            response.setAccessToken(jwtService.generateToken(response.getUsername()));
            response.setExpiresIn(((int) JwtService.EXPIRATION_TIME)/1000);
            return ApiResponse.success("Account verified successfully", response);

        }else{
            throw new AppException("INVALID_OTP", "Input otp is invalid.");
        }
    }
    public ApiResponse<LoginResponseDTO> login(LoginRequetDTO requestDTO){
        User user = userRepository.findByUsername(requestDTO.getUsername()).orElseThrow(()->new AppException("INVALID_CREDENTIALS","Wrong username or password."));

        boolean isMatch = passwordEncoder.matches(requestDTO.getPassword(), user.getHashedPassword());
        if(isMatch){
            LoginResponseDTO response = new LoginResponseDTO(jwtService.generateToken(user.getUsername()),(((int) JwtService.EXPIRATION_TIME)/1000), user.getUserId(), user.getUsername(), user.getDisplayName(), user.getGender(), user.getStatus(), user.getCreatedAt());
            return ApiResponse.success("Login successfully", response);
        }else{
            throw new AppException("INVALID_CREDENTIALS","Wrong username or password.");
        }

    }

    @Autowired
    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }
}
