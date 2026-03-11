package chattingappbackend.external_services.otp_services;

import chattingappbackend.models.OTPDetails;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OTPStorage {
    private final Map<String, OTPDetails> storage = new ConcurrentHashMap<>();


    public String generateOTP(String identifier, String target){
        String otpCode = String.valueOf((int)((Math.random() * 900000) + 100000));
        storage.put(identifier, new OTPDetails(otpCode,target, 10));
        return otpCode;
    }

    public OTPDetails get(String identifier) {
        return storage.get(identifier);
    }

    public void remove(String identifier) {
        storage.remove(identifier);
    }
    @Scheduled(fixedRate = 300000)
    public void cleanupExpiredOTP() {
        System.out.println("--- Removing expired OTP---");

        storage.entrySet().removeIf(entry -> {
            boolean expired = entry.getValue().isExpired();
            if (expired) {
                System.out.println("Removed OTP of user_id: " + entry.getKey());
            }
            return expired;
        });

        System.out.println("--- Removed expired OTP ---");
    }
}
