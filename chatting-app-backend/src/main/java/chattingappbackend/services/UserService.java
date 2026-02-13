package chattingappbackend.services;

import chattingappbackend.dtos.RegisterRequestDTO;
import chattingappbackend.dtos.RegisterResponseDTO;
import chattingappbackend.dtos.RegisterVerifyRequestDTO;
import chattingappbackend.dtos.RegisterVerifyResponseDTO;
import chattingappbackend.exceptions.AppException;
import chattingappbackend.external_services.otp_services.OTPService;
import chattingappbackend.models.User;
import chattingappbackend.models.UserStatus;
import chattingappbackend.repositories.UserRepository;
import chattingappbackend.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    public ApiResponse<Void> sendRegisterOTP(String username){
        if(userRepository.findByUsername(username).isEmpty()){
            throw new AppException("INVALID_USERNAME", "Input username is invalid.");
        }
        UserStatus currentStatus = userRepository.findStatusByUsername(username)
                .orElse(null);
        if(currentStatus!=UserStatus.UNVERIFIED){
            throw new AppException("ACCOUNT_ALREADY_VERIFIED", "Account have already been verified");
        }
        String phoneNumber = userRepository.findPhoneNumberByUsername(username).orElse(null);
        boolean sent = otpService.sendOTP(phoneNumber);
        if(sent) {
            return ApiResponse.success("Sent OTP successfully", null);
        }   else{
            throw new AppException("SEND_OTP_FAILED", "Could not send OTP");
        }
    }
    public RegisterVerifyResponseDTO verifyRegister(RegisterVerifyRequestDTO requestDTO){
        UserStatus userStatus = userRepository.findStatusByUsername(requestDTO.getUsername()).orElse(UserStatus.BANNED);
        if(userStatus!=UserStatus.UNVERIFIED){
            throw new AppException("ACCOUNT_ALREADY_VERIFIED","Account have already been verified");
        }
        String phoneNumber =  userRepository.findPhoneNumberByUsername(requestDTO.getUsername()).orElse(null);
        boolean verify = otpService.checkOTP(requestDTO.getOtp(), phoneNumber);
        if(verify){
            userRepository.updateStatusByUsername(requestDTO.getUsername(), UserStatus.ACTIVATED);
            RegisterVerifyResponseDTO response = userRepository.findUserForVerification(requestDTO.getUsername()).orElse(null);
            assert response != null;
            response.setAccessToken(jwtService.generateToken(response.getUsername()));
            response.setExpiresIn(((int) JwtService.EXPIRATION_TIME)/1000);
            return response;

        }else{
            throw new AppException("INVALID_OTP", "Input otp is invalid.");
        }
    }


    @Autowired
    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }
}
