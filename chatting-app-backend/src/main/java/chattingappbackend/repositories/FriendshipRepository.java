package chattingappbackend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import chattingappbackend.dtos.FriendRequestResponseDTO;
import chattingappbackend.models.Friendship;
import chattingappbackend.models.FriendshipStatus;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, String> {

    // 1. Tìm quan hệ giữa 2 user (JPQL)
    @Query("""
        SELECT f FROM Friendship f
        WHERE (f.userId = :userA AND f.friendId = :userB)
           OR (f.userId = :userB AND f.friendId = :userA)
    """)
    Optional<Friendship> findFriendshipBetween(
            @Param("userA") String userA,
            @Param("userB") String userB);


    // 2. Cập nhật trạng thái
    @Modifying
    @Transactional
    @Query("""
        UPDATE Friendship f
        SET f.status = :status
        WHERE f.friendshipId = :friendshipId
    """)
    void updateStatus(
            @Param("friendshipId") String friendshipId,
            @Param("status") FriendshipStatus status);

    // 3. Lấy tất cả danh sách lời mời kết bạn
    @Query("""
        SELECT new chattingappbackend.dtos.FriendRequestResponseDTO(
            f.friendshipId,
            u.userId,
            u.displayName,
            u.avatarUrl,
            f.status
        )
        FROM Friendship f
        JOIN User u ON f.userId = u.userId
        WHERE f.friendId = :userId AND f.status = 'PENDING'
    """)
    List<FriendRequestResponseDTO> findPendingRequests(@Param("userId") String userId);

    // 4. Lấy danh sách bạn bè
    @Query("""
        SELECT new chattingappbackend.dtos.FriendRequestResponseDTO(
            f.friendshipId,
            u.userId,
            u.displayName,
            u.avatarUrl,
            f.status
        )
        FROM Friendship f
        JOIN User u 
            ON (
                (f.userId = :userId AND u.userId = f.friendId)
                OR
                (f.friendId = :userId AND u.userId = f.userId)
            )
        WHERE f.status = 'ACCEPTED'
    """)
    List<FriendRequestResponseDTO> findFriend(@Param("userId") String userId);
}
