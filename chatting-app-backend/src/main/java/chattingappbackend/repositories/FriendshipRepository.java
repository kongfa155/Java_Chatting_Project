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

    // 1. Tìm quan hệ giữa 2 user
    @Query("SELECT * FROM friendships WHERE (user_id = :userA AND friend_id = :userB) OR (user_id = :userB AND friend_id = :userA)")
    Optional<Friendship> findFriendshipBetween(
            @Param("userA") String userA,
            @Param("userB") String userB);

    // 2. Tạo mối quan hệ bạn bè (Gửi lời mời)
    @Modifying
    @Query("INSERT INTO friendships (friendship_id, user_id, friend_id, status, created_at) "
            + "VALUES (:#{#f.friendshipId}, :#{#f.userId}, :#{#f.friendId}, :#{#f.status}, :#{#f.createdAt})")
    void insertFriendship(@Param("f") Friendship f);

    // 3. Cập nhật trạng thái (Chấp nhận/Xóa)
    @Modifying
    @Query("UPDATE friendships SET status = :status WHERE friendship_id = :friendshipId")
    void updateStatus(
            @Param("friendshipId") String friendshipId,
            @Param("status") FriendshipStatus status);

    // 4. Lấy tất cả lời mời kết bạn đang chờ (PENDING)
    @Query("""
    SELECT
       f.friendship_id,  -- Bỏ AS friendshipId
       u.user_id,         -- Bỏ AS userId
       u.display_name,
       u.avatar_url,
       f.status
    FROM friendships f
    JOIN users u ON f.user_id = u.user_id
    WHERE f.friend_id = :userId AND f.status = 'PENDING'
""")
    List<FriendRequestResponseDTO> findPendingRequests(@Param("userId") String userId);

    // 5. Lấy danh sách bạn bè đã đồng ý (ACCEPTED)
// 5. Lấy danh sách bạn bè đã đồng ý (ACCEPTED)
    @Query("""
    SELECT 
        f.friendship_id, 
        u.user_id, 
        u.display_name, 
        u.avatar_url, 
        f.status
    FROM friendships f
    JOIN users u 
        ON (
            (f.user_id = :userId AND u.user_id = f.friend_id)
            OR
            (f.friend_id = :userId AND u.user_id = f.user_id)
        )
    WHERE f.status = 'ACCEPTED'
""")
    List<FriendRequestResponseDTO> findFriend(@Param("userId") String userId);
}
