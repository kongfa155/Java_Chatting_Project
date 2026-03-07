package chattingappbackend.external_services.otp_services;

import chattingappbackend.models.OTPDetails;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OTPStorage {
    private final Map<String, OTPDetails> storage = new ConcurrentHashMap<>();

    public void save(String email, String code, int durationMinutes) {
        storage.put(email, new OTPDetails(code, durationMinutes));
    }
    public String generateOTP(String email){
        String otpCode = String.valueOf((int)((Math.random() * 900000) + 100000));
        storage.put(email, new OTPDetails(otpCode, 10));
        return otpCode;
    }

    public OTPDetails get(String email) {
        return storage.get(email);
    }

    public void remove(String email) {
        storage.remove(email);
    }
    @Scheduled(fixedRate = 300000)
    public void cleanupExpiredOTP() {
        System.out.println("--- Removing expired OTP---");

        storage.entrySet().removeIf(entry -> {
            boolean expired = entry.getValue().isExpired();
            if (expired) {
                System.out.println("Removed OTP of email: " + entry.getKey());
            }
            return expired;
        });

        System.out.println("--- Removed expired OTP ---");
    }
}
