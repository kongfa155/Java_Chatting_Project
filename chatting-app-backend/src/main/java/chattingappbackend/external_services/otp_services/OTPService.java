package chattingappbackend.external_services.otp_services;

public interface OTPService {
    public boolean sendOTP(String phoneNumber);
    public boolean checkOTP(String code, String phoneNumber);
}
