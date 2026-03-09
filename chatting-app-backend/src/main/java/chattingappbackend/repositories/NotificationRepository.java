package chattingappbackend.repositories;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import chattingappbackend.models.Notification;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, String> {

    // 1. Tạo thông báo
    @Modifying
    @Query("""
        INSERT INTO notifications
        (notification_id, user_id, content, is_read, is_deleted, created_at, type)
        VALUES (:#{#n.notificationId}, :#{#n.userId}, :#{#n.content},
        :#{#n.isRead}, :#{#n.isDeleted}, :#{#n.createdAt}, :#{#n.type})
    """)
    void insertNotification(@Param("n") Notification notification);

    // 2. Lấy danh sách thông báo
    @Query("""
        SELECT * FROM notifications
        WHERE user_id = :userId
        AND is_deleted = false
        ORDER BY created_at DESC
    """)
    List<Notification> findByUserId(@Param("userId") String userId);

}
