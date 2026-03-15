package chattingappbackend;

import chattingappbackend.config.TwilioConfig;
import chattingappbackend.repositories.FriendshipRepository;
import chattingappbackend.repositories.MessageRepository;
import chattingappbackend.repositories.NotificationRepository;
import chattingappbackend.repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EntityScan(basePackages = "chattingappbackend.models")
// 1. DÀNH CHO JPA: Quản lý User và Message. Cấm quét Friendship và Notification.
@EnableJpaRepositories(
        basePackages = "chattingappbackend.repositories",
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {FriendshipRepository.class, NotificationRepository.class})
)
// 2. DÀNH CHO JDBC: Quản lý Friendship và Notification. Cấm quét User và Message.
@EnableJdbcRepositories(
        basePackages = "chattingappbackend.repositories",
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {UserRepository.class, MessageRepository.class})
)
public class ChattingAppBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChattingAppBackendApplication.class, args);
    }

}
