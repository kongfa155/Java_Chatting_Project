package chattingappbackend.external_services.otp_services;

public interface OTPService {
    public boolean sendOTP(String email);
    public boolean checkOTP(String code, String email);
}
