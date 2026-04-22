package chattingappbackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import chattingappbackend.models.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    // Lấy danh sách thông báo
    List<Notification> findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(String userId);

}
