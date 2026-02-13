package chattingappbackend.external_services.otp_services;

import chattingappbackend.config.TwilioConfig;
import chattingappbackend.utils.PhoneNumberHelper;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwilioService implements OTPService{

    private final TwilioConfig twilioConfig;

    @Autowired
    public TwilioService(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
    }

    @Override
    public boolean sendOTP(String rawPhoneNumber) {
        try {
            String phoneNumber = PhoneNumberHelper.formatPhoneNumber(rawPhoneNumber);
            Verification verification = Verification.creator(
                            twilioConfig.getVerifyServiceSid(),
                            phoneNumber,
                            "sms")
                    .create();
            return "pending".equals(verification.getStatus());
        } catch (Exception e) {
            System.err.println("OTP Send Error: " + e.getMessage());
            return false;
        }
    }
    @Override
    public boolean checkOTP(String code, String rawPhoneNumber){
        try {
            String phoneNumber = PhoneNumberHelper.formatPhoneNumber(rawPhoneNumber);
            VerificationCheck verificationCheck = VerificationCheck.creator(
                            twilioConfig.getVerifyServiceSid())
                    .setTo(phoneNumber)
                    .setCode(code)
                    .create();
            return "approved".equals(verificationCheck.getStatus());
        } catch (Exception e) {
            System.err.println("OTP Check Error: " + e.getMessage());
            return false;
        }
    }
}
