package chattingappbackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import chattingappbackend.models.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    // Lấy danh sách thông báo
    //Tìm thông báo của người dùng có id = id truyền vào, trạng thái bị xóa là false và sắp xếp giảm dần theo thời gian tạo
    List<Notification> findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(String userId);

}
