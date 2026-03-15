package chattingappbackend.dtos.user.register;

import chattingappbackend.models.UserStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record RegisterVerifyResponseDTO(
        @JsonProperty("access_token")
        String accessToken,

        @JsonProperty("expires_in")
        int expiresIn,

        @JsonProperty("user_id")
        String userId,

        @JsonProperty("username")
        String username,

        @JsonProperty("display_name")
        String displayName,

        @JsonProperty("gender")
        boolean gender,

        @JsonProperty("status")
        UserStatus status,

        @JsonProperty("created_at")
        Instant createdAt
) {
        public RegisterVerifyResponseDTO(String userId, String username, String displayName, boolean gender, UserStatus status, Instant createdAt) {
                this(null, 0, userId, username, displayName, gender, status, createdAt);
        }
}