package chattingappbackend.repositories;

import chattingappbackend.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String> {

    List<Message> findBySenderIdAndReceiverId(String senderId, String receiverId);

    List<Message> findByReceiverIdAndSenderId(String receiverId, String senderId);
}