package chattingappbackend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import chattingappbackend.dtos.FriendRequestResponseDTO;
import chattingappbackend.models.Friendship;
import chattingappbackend.models.FriendshipStatus;

@Repository
public interface FriendshipRepository extends CrudRepository<Friendship, String> {

    // Tìm quan hệ giữa 2 user
    @Query("""
                    SELECT * FROM friendships
                    WHERE (user_id = :userA AND friend_id = :userB)
                    OR (user_id = :userB AND friend_id = :userA)
            """)
    Optional<Friendship> findFriendshipBetween(
            @Param("userA") String userA,
            @Param("userB") String userB);
    
    // Tạo mối quan hệ bạn bè, dùng để gửi lời mời kết bạn
    @Modifying
    @Query("""
            INSERT INTO friendships (friendship_id, user_id, friend_id, status, created_at)
            VALUES (:#{#f.friendshipId}, :#{#f.userId}, :#{#f.friendId},:#{#f.status}, :#{#f.createdAt})
            """)
    void insertFriendship(@Param("f") Friendship f);

    // Cập nhật quan hệ bạn bè, dùng khi xóa, chấp nhận
    @Modifying
    @Query("""
            UPDATE friendships
            SET status = :status
            WHERE friendship_id = :friendshipId
            """)
    void updateStatus(
            @Param("friendshipId") String friendshipId,
            @Param("status") FriendshipStatus status);

    // Lấy tất cả lời mời kết bạn (Lấy status = pending)
    @Query("""
            SELECT f.friendship_id,
                   u.user_id,
                   u.display_name,
                   u.avatar_url,
                   f.status
            FROM friendships f
            JOIN users u ON f.user_id = u.user_id
            WHERE f.friend_id = :userId
            AND f.status = 'PENDING'
            """)
    List<FriendRequestResponseDTO> findPendingRequests(@Param("userId") String userId);

    //Lấy danh sách bạn bè đã accept (Lấy status = Accept)
    @Query("""
            SELECT f.friendship_id,
                   u.user_id,
                   u.display_name,
                   u.avatar_url,
                   f.status
            FROM friendships f
            JOIN users u ON f.user_id = u.user_id
            WHERE f.friend_id = :userId
            AND f.status = 'ACCEPTED'
            """)
    List<FriendRequestResponseDTO> findFriend(@Param("userId") String userId);
}
