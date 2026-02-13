package chattingappbackend;

import chattingappbackend.config.TwilioConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class ChattingAppBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChattingAppBackendApplication.class, args);
    }

}
