package chattingappbackend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="twilio")
public class TwilioConfig {
    private String accountSid;
    private String authToken;
    private String verifyServiceSid;

    public TwilioConfig() {
    }

    public TwilioConfig(String accountSid, String authToken, String verifyServiceSid) {
        this.accountSid = accountSid;
        this.authToken = authToken;
        this.verifyServiceSid = verifyServiceSid;

    }

    public String getVerifyServiceSid() {
        return verifyServiceSid;
    }

    public void setVerifyServiceSid(String verifyServiceSid) {
        this.verifyServiceSid = verifyServiceSid;
    }

    public String getAccountSid() {
        return accountSid;
    }

    public void setAccountSid(String accountSid) {
        this.accountSid = accountSid;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
