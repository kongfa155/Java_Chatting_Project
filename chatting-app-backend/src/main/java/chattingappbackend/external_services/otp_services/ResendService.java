package chattingappbackend.external_services.otp_services;


import chattingappbackend.exceptions.AppException;
import chattingappbackend.models.OTPDetails;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Primary
@Service
public class ResendService implements OTPService {
    Resend resend;
    OTPStorage otpStorage;

    @Autowired
    public ResendService(Resend resend, OTPStorage otpStorage) {
        this.resend = resend;
        this.otpStorage = otpStorage;
    }

    public ResendService() {
    }

    public Resend getResend() {
        return resend;
    }

    public void setResend(Resend resend) {
        this.resend = resend;
    }

    @Override
    public boolean sendOTP(String email) {
        String otpCode = otpStorage.generateOTP(email);
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("JavaChattingApp <onboarding@nhhun2005.id.vn>")
                .to(email)
                .subject("[JavaChattingApp] - Email verification")
                .html("<h1>Chào mừng đến với JavaChattingApp!</h1><h2>Mã OTP của bạn là: <b>" + otpCode + "</b> (Hết hạn sau 10 phút)</h2>")
                
                .build();
        try{
            CreateEmailResponse data = resend.emails().send(params);
            return true;
        }catch (ResendException e){
            otpStorage.remove(email);
                e.printStackTrace();
                return false;
        }

    }

    @Override
    public boolean checkOTP(String code, String email) {
        OTPDetails details = otpStorage.get(email);
        if(details==null|| details.isExpired()){
            otpStorage.remove(email);
            return false;
        }
        if (details.getCode().equals(code)) {
            otpStorage.remove(email);
            return true;
        }

        return false;
    }
}
