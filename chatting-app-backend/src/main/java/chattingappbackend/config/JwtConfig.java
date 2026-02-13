package chattingappbackend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="jwt")
public class JwtConfig {
    private static String secretKey;

    public JwtConfig(String secretKey) {
        JwtConfig.secretKey = secretKey;
    }

    public JwtConfig() {
    }

    public static String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        JwtConfig.secretKey = secretKey;
    }
}
