package chattingappbackend.repositories;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import chattingappbackend.dtos.RegisterVerifyResponseDTO;
import chattingappbackend.models.User;
import chattingappbackend.models.UserStatus;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    @Query("SELECT * FROM users WHERE user_id = :userId")
    Optional<User> findByUserId(@Param("userId") String userId);
    @Query("SELECT * FROM users WHERE username = :username")
    Optional<User> findByUsername(@Param("username") String username);
    @Query("SELECT phone_number FROM users WHERE username = :username")
    Optional<String> findPhoneNumberByUsername(@Param("username") String username);
    @Query("SELECT email FROM users WHERE username= :username")
    Optional<String> findEmailByUsername(@Param("username") String username);
    @Query("SELECT COUNT(*) > 0 FROM users WHERE phone_number = :phoneNumber")
    boolean existsByPhoneNumber(@Param("phoneNumber") String phoneNumber);
    @Query("SELECT COUNT(*) > 0 FROM users WHERE email =:email")
    boolean existsByEmail(@Param("email") String email);
    @Query("SELECT status FROM users WHERE username = :username")
    Optional<UserStatus> findStatusByUsername(@Param("username") String username);

    @Modifying
    @Query("INSERT INTO users (user_id, gender, username, email, display_name, avatar_url, password, status, created_at) " +
            "VALUES (:#{#u.userId}, :#{#u.gender}, :#{#u.username}, :#{#u.email}, :#{#u.displayName}, :#{#u.avatarUrl}, :#{#u.hashedPassword}, :#{#u.status}, :#{#u.createdAt})")
    void insertUser(@Param("u") User u);
    @Modifying
    @Query("UPDATE users SET status = :status WHERE username = :username")
    void updateStatusByUsername(@Param("username") String username, @Param("status") UserStatus status);
    @Modifying
    @Query("UPDATE users SET password = :password WHERE user_id = :userId")
    void updatePasswordByUserId(@Param("userId") String userId, @Param("password") String password);

    @Query("SELECT user_id, username, display_name, gender, status, created_at FROM users WHERE username = :username")
    Optional<RegisterVerifyResponseDTO> findUserForVerification(@Param("username") String username);

    @Query("SELECT * FROM users WHERE phone_number= :phone_number")
    Optional<User> findByPhoneNumber(@Param("phone_number")  String phoneNumber);

    @Query("SELECT * FROM users WHERE phone_number= :email")
    Optional<User> findByEmail(@Param("email") String email);
}
