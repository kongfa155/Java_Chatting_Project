package chattingappbackend.repositories;

import chattingappbackend.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String> {

    // Lấy toàn bộ conversation giữa 2 user
    List<Message> findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(
            String sender1, String receiver1,
            String sender2, String receiver2
    );

    // Lấy tất cả tin nhắn chưa đọc của user
    List<Message> findByReceiverIdAndIsReadFalse(String receiverId);

}