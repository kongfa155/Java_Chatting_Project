package chattingappbackend.externalservices.otpservices;


import chattingappbackend.models.OTPDetails;
import chattingappbackend.services.JwtService;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class ResendService implements OTPService {
    Resend resend;
    OTPStorage otpStorage;
    JwtService jwtService;

    @Autowired
    public ResendService(Resend resend, OTPStorage otpStorage, JwtService jwtService) {
        this.resend = resend;
        this.otpStorage = otpStorage;
        this.jwtService = jwtService;

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
    public boolean sendOTP(String identifier, String target) {
        String otpCode = otpStorage.generateOTP(identifier, target);
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("JavaChattingApp <onboarding@nhhun2005.id.vn>")
                .to(target)
                .subject("[JavaChattingApp] - Email verification")
                .html("<h1>Chào mừng đến với JavaChattingApp!</h1><h2>Mã OTP của bạn là: <b>" + otpCode + "</b> (Hết hạn sau 10 phút)</h2>")
                
                .build();
        try{
            CreateEmailResponse data = resend.emails().send(params);
            return true;
        }catch (ResendException e){
            otpStorage.remove(identifier);
                e.printStackTrace();
                return false;
        }

    }

    @Override
    public boolean checkOTP(String identifier, String code, String email) {
        OTPDetails details = otpStorage.get(identifier);
        if (details == null) {
            System.out.println("DEBUG: Không tìm thấy OTP cho user: " + identifier);
            return false;
        }
        if (details.isExpired()) {
            otpStorage.remove(identifier);
            System.out.println("DEBUG: OTP đã hết hạn cho user: " + identifier);
            return false;
        }

        if (!details.getTarget().equals(email)) {
            System.err.println("WARNING: Đối soát Email thất bại! Target lưu: "
                    + details.getTarget() + " nhưng input là: " + email);
            return false;
        }

        if (details.getCode().equals(code)) {
            otpStorage.remove(identifier);
            return true;
        }

        return false;
    }
}
