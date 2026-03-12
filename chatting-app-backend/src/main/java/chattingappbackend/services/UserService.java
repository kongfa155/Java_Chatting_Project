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

    public ApiResponse<RegisterResponseDTO> register(RegisterRequestDTO requestDTO) {
        if (userRepository.findByUsername(requestDTO.getUsername()).isPresent()) {
            throw new AppException("USERNAME_EXISTS", "Input username have been used, please use another username.");
        }
        if (userRepository.existsByEmail(requestDTO.getEmail())) {
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
        return ApiResponse.success("User registered successfully", new RegisterResponseDTO(user.getDisplayName(), user.getStatus()));
    }

    public ApiResponse<Void> sendRegisterOTP(String username) {
        if (userRepository.findByUsername(username).isEmpty()) {
            throw new AppException("INVALID_USERNAME", "Input username is invalid.");
        }
        UserStatus currentStatus = userRepository.findStatusByUsername(username)
                .orElse(null);
        if (currentStatus != UserStatus.UNVERIFIED) {
            throw new AppException("ACCOUNT_ALREADY_VERIFIED", "Account have already been verified");
        }
        String email = userRepository.findEmailByUsername(username).orElse(null);
        boolean sent = otpService.sendOTP(email, email);
        if (sent) {
            return ApiResponse.success("Sent OTP successfully", null);
        } else {
            throw new AppException("SEND_OTP_FAILED", "Could not send OTP");
        }
    }

    public ApiResponse<RegisterVerifyResponseDTO> verifyRegister(RegisterVerifyRequestDTO requestDTO) {
        UserStatus userStatus = userRepository.findStatusByUsername(requestDTO.getUsername()).orElseThrow(() -> new AppException("INVALID_USERNAME", "Input username is invalid."));
        if (userStatus != UserStatus.UNVERIFIED) {
            throw new AppException("ACCOUNT_ALREADY_VERIFIED", "Account have already been verified");
        }
        String email = userRepository.findEmailByUsername(requestDTO.getUsername()).orElse(null);
        boolean verify = otpService.checkOTP(email, requestDTO.getOtp(), email);
        if (verify) {
            userRepository.updateStatusByUsername(requestDTO.getUsername(), UserStatus.ACTIVATED);
            RegisterVerifyResponseDTO response = userRepository.findUserForVerification(requestDTO.getUsername()).orElseThrow(() -> new AppException("INVALID_USERNAME", "Input username is invalid."));
            response.setAccessToken(jwtService.generateToken(response.getUsername(), response.getUserId()));
            response.setExpiresIn(((int) JwtService.EXPIRATION_TIME) / 1000);
            return ApiResponse.success("Account verified successfully", response);

        } else {
            throw new AppException("INVALID_OTP", "Input otp is invalid.");
        }
    }

    public ApiResponse<LoginResponseDTO> login(LoginRequetDTO requestDTO) {
        User user = userRepository.findByUsername(requestDTO.getUsername()).orElseThrow(() -> new AppException("INVALID_CREDENTIALS", "Wrong username or password."));
        if (user.getStatus() != UserStatus.ACTIVATED) {
            throw new AppException("ACCOUNT_IS_NOT_ACTIVATED", "Account status is not activated.");
        }
        boolean isMatch = passwordEncoder.matches(requestDTO.getPassword(), user.getHashedPassword());
        if (isMatch) {
            LoginResponseDTO response = new LoginResponseDTO(jwtService.generateToken(user.getUsername(), user.getUserId()), (((int) JwtService.EXPIRATION_TIME) / 1000), user.getUserId(), user.getUsername(), user.getDisplayName(), user.getGender(), user.getStatus(), user.getCreatedAt());
            return ApiResponse.success("Login successfully", response);
        } else {
            throw new AppException("INVALID_CREDENTIALS", "Wrong username or password.");
        }

    }

    public ApiResponse<Void> changePassword(String jwt, ChangePasswordRequestDTO dto) {
        String userIdFromJWT = jwtService.extractUserId(jwt);

        User user = userRepository.findByUserId(userIdFromJWT).orElseThrow(() -> new AppException("USER_NOT_EXISTS", "Can't find this user"));
        boolean isMatch = passwordEncoder.matches(dto.getOldPassword(), user.getHashedPassword());
        if (isMatch) {
            userRepository.updatePasswordByUserId(userIdFromJWT, passwordEncoder.encode(dto.getNewPassword()));
            return ApiResponse.success("Changed password successfully", null);
        } else {
            throw new AppException("WRONG_OLD_PASSWORD", "Wrong old password.");
        }

    }
    public ApiResponse<Void> getChangeEmailOTP(String jwt, ChangeEmailOTPRequestDTO dto){
        String userId = jwtService.extractUserId(jwt);
        User user = userRepository.findByUserId(userId).orElseThrow(()-> new AppException("USER_NOT_EXISTS", "Can't find this user"));
        boolean isMatch = passwordEncoder.matches(dto.getPassword(), user.getHashedPassword());
        if(!isMatch){
            throw new AppException("INVALID_CREDENTIALS", "Wrong password");
        }else{
            if(userRepository.existsByEmail(dto.getNewEmail())){
                throw new AppException("EMAIL_EXISTS", "Email exists");
            }else{
                otpService.sendOTP(userId,dto.getNewEmail());
                return ApiResponse.success("Sent OTP successfully", null);
            }
        }
    }
    public ApiResponse<Void> changeEmail(String jwt, ChangeEmailRequestDTO dto){
        String userId = jwtService.extractUserId(jwt);
        User user = userRepository.findByUserId(userId).orElseThrow(()-> new AppException("USER_NOT_EXISTS", "Can't find this user"));

        boolean isMatch = otpService.checkOTP(userId, dto.getOtp(), dto.getNewEmail());
        if(!isMatch){
            throw new AppException("INVALID_OTP", "Input OTP is invalid");
        }else{
            if(userRepository.existsByEmail(dto.getNewEmail())){
                throw new AppException("EMAIL_EXISTS", "Email exists");
            }else{
                userRepository.updateEmailByUserId(userId, dto.getNewEmail());
                return ApiResponse.success("Changed email successfully", null);
            }

        }
    }
    public ApiResponse<ChangeProfileResponseDTO> changeProfile(String jwt, ChangeProfileRequestDTO dto){
        String userId = jwtService.extractUserId(jwt);
        User user = userRepository.findByUserId(userId).orElseThrow(()-> new AppException("USER_NOT_FOUND", "This user is not exists"));
        boolean isMatch = passwordEncoder.matches(dto.getPassword(), user.getHashedPassword());
        if(!isMatch){
            throw new AppException("INVALID_CREDENTIALS", "Wrong password");
        }else{
            user.setDisplayName(dto.getDisplayName());
            user.setGender(dto.isGender());
            user.setAvatarUrl(dto.getAvatarURL());
            userRepository.updateProfileByUserId(userId,user);
            return ApiResponse.success("Change display information successfully", null);
        }

    }

    @Autowired
    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public ApiResponse<SearchDTO> findByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("USER_NOT_FOUND", "This user is not exists"));

        SearchDTO dto = new SearchDTO();
        dto.setUserId(user.getUserId());
        dto.setDisplayName(user.getDisplayName());
        dto.setEmail(user.getEmail());
        dto.setAvatarUrl(user.getAvatarUrl());

        return ApiResponse.success("User found", dto);
    }

}
