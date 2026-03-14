package chattingappbackend.repositories;

import chattingappbackend.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String> {

    @Query("""
    SELECT m FROM Message m
    WHERE (m.senderId = :userA AND m.receiverId = :userB)
       OR (m.senderId = :userB AND m.receiverId = :userA)
    ORDER BY m.sentAt
    """)
    List<Message> findConversation(String userA, String userB);

    List<Message> findByReceiverIdAndIsReadFalse(String receiverId);
}