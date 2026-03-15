package chattingappbackend.repositories;

import chattingappbackend.dtos.user.register.RegisterVerifyResponseDTO;
import chattingappbackend.models.User;
import chattingappbackend.models.UserStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    // Hibernate tự hiểu: SELECT * FROM users WHERE user_id = ?
    Optional<User> findByUserId(String userId);

    // Hibernate tự hiểu: SELECT * FROM users WHERE username = ?
    Optional<User> findByUsername(String username);

    // SQL gốc: SELECT email FROM users WHERE username = ?
    @Query("SELECT u.email FROM User u WHERE u.username = :username")
    Optional<String> findEmailByUsername(@Param("username") String username);

    // Hibernate tự hiểu: SELECT COUNT(*) > 0 FROM users WHERE email = ?
    boolean existsByEmail(String email);

    // SQL gốc: SELECT status FROM users WHERE username = ?
    @Query("SELECT u.status FROM User u WHERE u.username = :username")
    Optional<UserStatus> findStatusByUsername(@Param("username") String username);

    /**
     * THAY THẾ insertUser:
     * Trong JPA, không dùng @Query để INSERT.
     * Bạn chỉ cần gọi userRepository.save(user) trong Service.
     * Hibernate sẽ tự sinh lệnh INSERT.
     */

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.status = :status WHERE u.username = :username")
    void updateStatusByUsername(@Param("username") String username, @Param("status") UserStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.hashedPassword = :password WHERE u.userId = :userId")
    void updatePasswordByUserId(@Param("userId") String userId, @Param("password") String password);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.email = :newEmail WHERE u.userId = :userId")
    void updateEmailByUserId(@Param("userId") String userId, @Param("newEmail") String newEmail);

    /**
     * Lưu ý: Với findUserForVerification, vì bạn trả về một DTO (RegisterVerifyResponseDTO)
     * không phải Entity, bạn nên dùng Projection hoặc Mapping thủ công ở Service.
     * Nếu vẫn muốn dùng Query:
     */
    @Query("SELECT new chattingappbackend.dtos.user.register.RegisterVerifyResponseDTO(u.userId, u.username, u.displayName, u.gender, u.status, u.createdAt) " +
            "FROM User u WHERE u.username = :username")
    Optional<RegisterVerifyResponseDTO> findUserForVerification(@Param("username") String username);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.displayName = :displayName, " +
            "u.gender = :gender, " +
            "u.avatarUrl = :avatarUrl " +
            "WHERE u.userId = :userId")
    void updateProfileByUserId(@Param("userId") String userId,
                               @Param("displayName") String displayName,
                               @Param("gender") boolean gender,
                               @Param("avatarUrl") String avatarUrl);

    // Hibernate tự hiểu: SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);
}