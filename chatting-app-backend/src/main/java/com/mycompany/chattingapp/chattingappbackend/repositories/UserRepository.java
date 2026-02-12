package com.mycompany.chattingapp.chattingappbackend.repositories;

import com.mycompany.chattingapp.chattingappbackend.models.User;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    @Query("SELECT * FROM users WHERE username = :username")
    Optional<User> findByUsername(@Param("username") String username);
    @Query("SELECT COUNT(*) > 0 FROM users WHERE phone_number = :phoneNumber")
    boolean existsByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Modifying
    @Query("INSERT INTO users (user_id, gender, username, phone_number, display_name, avatar_url, password, status, created_at) " +
            "VALUES (:#{#u.userId}, :#{#u.gender}, :#{#u.username}, :#{#u.phoneNumber}, :#{#u.displayName}, :#{#u.avatarUrl}, :#{#u.hashedPassword}, :#{#u.status}, :#{#u.createdAt})")
    void insertUser(@Param("u") User u);

}
