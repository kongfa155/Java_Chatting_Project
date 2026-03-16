package chattingappbackend.services;

import chattingappbackend.dtos.*;
import chattingappbackend.dtos.user.changeemail.ChangeEmailOTPRequestDTO;
import chattingappbackend.dtos.user.changeemail.ChangeEmailRequestDTO;
import chattingappbackend.dtos.user.changepassword.ChangePasswordRequestDTO;
import chattingappbackend.dtos.user.changeprofile.ChangeProfileRequestDTO;
import chattingappbackend.dtos.user.changeprofile.ChangeProfileResponseDTO;
import chattingappbackend.dtos.user.forgotpassword.ForgotPasswordOTPRequestDTO;
import chattingappbackend.dtos.user.forgotpassword.ForgotPasswordVerifyRequest;
import chattingappbackend.dtos.user.login.LoginRequetDTO;
import chattingappbackend.dtos.user.login.LoginResponseDTO;
import chattingappbackend.dtos.user.register.RegisterRequestDTO;
import chattingappbackend.dtos.user.register.RegisterResponseDTO;
import chattingappbackend.dtos.user.register.RegisterVerifyRequestDTO;
import chattingappbackend.dtos.user.register.RegisterVerifyResponseDTO;
import chattingappbackend.exceptions.AppException;
import chattingappbackend.externalservices.otpservices.OTPService;
import chattingappbackend.models.User;
import chattingappbackend.models.UserStatus;
import chattingappbackend.repositories.UserRepository;
import chattingappbackend.responses.ApiResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;
    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;
    private OTPService otpService;
    private JwtService jwtService;

    public UserService(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

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

    @Transactional
    public ApiResponse<RegisterResponseDTO> register(RegisterRequestDTO requestDTO) {
        if (userRepository.existsByUsername(requestDTO.username())) {
            throw new AppException("USERNAME_EXISTS", "Input username have been used, please use another username.");
        }
        if (userRepository.existsByEmail(requestDTO.email())) {
            throw new AppException("EMAIL_EXISTS", "Input email have been used, please use another email.");
        }
        User user = new User(
                UUID.randomUUID().toString(),
                requestDTO.gender(),
                requestDTO.username(),
                requestDTO.email(),
                requestDTO.displayName(),
                requestDTO.avatarUrl(),
                passwordEncoder.encode(requestDTO.password()),
                UserStatus.UNVERIFIED,
                Instant.now()
        );

        userRepository.save(user);
        return ApiResponse.success("User registered successfully", new RegisterResponseDTO(user.getDisplayName(), user.getStatus()));
    }

    public ApiResponse<Void> sendRegisterOTP(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(()-> new AppException("INVALID_USERNAME", "Input username is invalid."));
        if (user.getStatus() != UserStatus.UNVERIFIED) {
            throw new AppException("ACCOUNT_ALREADY_VERIFIED", "Account have already been verified");
        }
        String otpKey = "REGISTER_"+user.getEmail();
        boolean sent = otpService.sendOTP(otpKey, user.getEmail());
        if (sent) {
            return ApiResponse.success("Sent OTP successfully", null);
        } else {
            throw new AppException("SEND_OTP_FAILED", "Could not send OTP");
        }
    }
    @Transactional
    public ApiResponse<RegisterVerifyResponseDTO> verifyRegister(RegisterVerifyRequestDTO requestDTO) {
        User user = userRepository.findByUsername(requestDTO.username()).orElseThrow(()-> new AppException("INVALID_USERNAME", "Input username is invalid."));
        if (user.getStatus() != UserStatus.UNVERIFIED) {
            throw new AppException("ACCOUNT_ALREADY_VERIFIED", "Account have already been verified");
        }
        String otpKey = "REGISTER_"+user.getEmail();
        boolean verify = otpService.checkOTP(otpKey, requestDTO.otp(), user.getEmail());
        if (verify) {
           user.setStatus(UserStatus.ACTIVATED);
            RegisterVerifyResponseDTO response = new RegisterVerifyResponseDTO(
                    jwtService.generateToken(user.getUsername(), user.getUserId()),
                    ((int) JwtService.EXPIRATION_TIME) / 1000,
                    user.getUserId(),
                    user.getUsername(),
                    user.getDisplayName(),
                    user.getGender(),
                    user.getStatus(),
                    user.getCreatedAt()
            );
            return ApiResponse.success("Account verified successfully", response);

        } else {
            throw new AppException("INVALID_OTP", "Input otp is invalid.");
        }
    }

    public ApiResponse<LoginResponseDTO> login(LoginRequetDTO requestDTO) {
        User user = userRepository.findByUsername(requestDTO.username()).orElseThrow(() -> new AppException("INVALID_CREDENTIALS", "Wrong username or password."));
        if (user.getStatus() != UserStatus.ACTIVATED) {
            throw new AppException("ACCOUNT_IS_NOT_ACTIVATED", "Account status is not activated.");
        }
        boolean isMatch = passwordEncoder.matches(requestDTO.password(), user.getHashedPassword());
        if (isMatch) {
            LoginResponseDTO response = new LoginResponseDTO(jwtService.generateToken(user.getUsername(), user.getUserId()), (((int) JwtService.EXPIRATION_TIME) / 1000), user.getUserId(), user.getUsername(), user.getDisplayName(), user.getGender(), user.getStatus(), user.getCreatedAt());
            return ApiResponse.success("Login successfully", response);
        } else {
            throw new AppException("INVALID_CREDENTIALS", "Wrong username or password.");
        }
    }
    @Transactional
    public ApiResponse<Void> changePassword(String userId, ChangePasswordRequestDTO dto) {

        User user = userRepository.findByUserId(userId).orElseThrow(() -> new AppException("USER_NOT_EXISTS", "Can't find this user"));
        boolean isMatch = passwordEncoder.matches(dto.oldPassword(), user.getHashedPassword());
        if (isMatch) {
            user.setHashedPassword(passwordEncoder.encode(dto.newPassword()));
            return ApiResponse.success("Changed password successfully", null);
        } else {
            throw new AppException("WRONG_OLD_PASSWORD", "Wrong old password.");
        }

    }

    public ApiResponse<Void> getChangeEmailOTP(String userId, ChangeEmailOTPRequestDTO dto){
        User user = userRepository.findByUserId(userId).orElseThrow(()-> new AppException("USER_NOT_EXISTS", "Can't find this user"));
        if (!passwordEncoder.matches(dto.password(), user.getHashedPassword())) {
            throw new AppException("INVALID_CREDENTIALS", "Wrong password");
        }
        if (userRepository.existsByEmail(dto.newEmail())) {
            throw new AppException("EMAIL_EXISTS", "Email exists");
        }
        String otpKey = "CHANGE_EMAIL_" +userId +"_"+dto.newEmail();

        otpService.sendOTP(otpKey, dto.newEmail());
        return ApiResponse.success("Sent OTP successfully", null);
    }
    @Transactional
    public ApiResponse<Void> changeEmail(String userId, ChangeEmailRequestDTO dto){
        User user = userRepository.findByUserId(userId).orElseThrow(()-> new AppException("USER_NOT_EXISTS", "Can't find this user"));
        String otpKey = "CHANGE_EMAIL_" + userId + "_" + dto.newEmail();
        boolean isMatch = otpService.checkOTP(otpKey, dto.otp(), dto.newEmail());
        if(!isMatch){
            throw new AppException("INVALID_OTP", "Input OTP is invalid");
        }
        if(userRepository.existsByEmail(dto.newEmail())){
            throw new AppException("EMAIL_EXISTS", "Email exists");
        }
        user.setEmail(dto.newEmail());
        return ApiResponse.success("Changed email successfully", null);


    }

    @Transactional
    public ApiResponse<ChangeProfileResponseDTO> changeProfile(String userId, ChangeProfileRequestDTO dto){
        User user = userRepository.findByUserId(userId).orElseThrow(()-> new AppException("USER_NOT_FOUND", "This user is not exists"));
        boolean isMatch = passwordEncoder.matches(dto.password(), user.getHashedPassword());
        if(!isMatch){
            throw new AppException("INVALID_CREDENTIALS", "Wrong password");
        }
        user.setDisplayName(dto.displayName());
        user.setGender(dto.gender());
        user.setAvatarUrl(dto.avatarUrl());
        return ApiResponse.success("Change display information successfully", null);

    }

    public ApiResponse<Void> forgotPasswordOTP(ForgotPasswordOTPRequestDTO dto){
        User user = userRepository.findByUsername(dto.username()).orElseThrow(()->new AppException("INVALID_USERNAME", "Input username is invalid"));
        String otpKey = "FORGOT_PASSWORD_"+user.getUserId();
        otpService.sendOTP(otpKey,user.getEmail());
        return ApiResponse.success("Sent OTP successfully", null);
    }

    @Transactional
    public ApiResponse<Void> forgotPasswordVerify(ForgotPasswordVerifyRequest dto){
        User user = userRepository.findByUsername(dto.username()).orElseThrow(()->new AppException("INVALID_USERNAME", "Input username is invalid"));
        String otpKey = "FORGOT_PASSWORD_"+user.getUserId();
        boolean isMatch = otpService.checkOTP(otpKey, dto.otp(), user.getEmail());
        if(isMatch){
            user.setHashedPassword(passwordEncoder.encode(dto.newPassword()));
            return ApiResponse.success("Changed password successfully", null);
        }else{
            throw new AppException("INVALID_OTP","Wrong otp");
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