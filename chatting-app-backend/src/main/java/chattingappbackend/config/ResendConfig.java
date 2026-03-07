package chattingappbackend.config;

import com.resend.Resend;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="resend")
public class ResendConfig {
    private String apiKey;

    public ResendConfig(String apiKey) {
        this.apiKey = apiKey;
    }

    public ResendConfig() {
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    @Bean
    public Resend resend(){
        return new Resend(this.apiKey);
    }
}
