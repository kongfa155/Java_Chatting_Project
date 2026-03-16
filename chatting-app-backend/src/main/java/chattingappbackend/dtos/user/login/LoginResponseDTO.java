package chattingappbackend.dtos.user.login;

import chattingappbackend.models.UserStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record LoginResponseDTO(
        @NotBlank
        @JsonProperty("access_token")
        String accessToken,

        @JsonProperty("expires_in")
        int expiresIn,

        @NotBlank
        @JsonProperty("user_id")
        String userId,

        @NotBlank
        @JsonProperty("username")
        String username,

        @NotBlank
        @JsonProperty("display_name")
        String displayName,

        @JsonProperty("gender")
        boolean gender,

        @NotNull
        @JsonProperty("status")
        UserStatus status,

        @JsonProperty("created_at")
        Instant createdAt,
        @NotNull
        @JsonProperty("email")
        @Email
        String email,
        @NotNull
        @JsonProperty("avatar_url")
        String avatarUrl
) {}