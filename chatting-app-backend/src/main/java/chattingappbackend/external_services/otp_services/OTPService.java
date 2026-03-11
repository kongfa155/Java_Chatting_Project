package chattingappbackend.external_services.otp_services;

public interface OTPService {
    public boolean sendOTP(String identifier, String target);
    public boolean checkOTP(String identifier, String code, String email);
}
