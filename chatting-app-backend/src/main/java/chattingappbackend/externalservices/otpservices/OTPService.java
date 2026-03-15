package chattingappbackend.externalservices.otpservices;

public interface OTPService {
    public boolean sendOTP(String identifier, String target);
    public boolean checkOTP(String identifier, String code, String email);
}
