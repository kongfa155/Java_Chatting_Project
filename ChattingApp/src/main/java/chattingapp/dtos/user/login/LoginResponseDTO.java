package chattingapp.dtos.user.login;

import chattingapp.models.UserStatus;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record LoginResponseDTO(
        @SerializedName("access_token")
        @NotBlank
        String accessToken,
        @SerializedName("expires_in")
        int expiresIn,
        
        @SerializedName("user_id")
        @NotBlank
        String userId,
        @SerializedName("username")
        @NotBlank
        String username,
        @SerializedName("display_name")
        @NotBlank
        String displayName,
        @SerializedName("gender")
        boolean gender,
        @SerializedName("status")
        @NotNull
        UserStatus status,
        @SerializedName("created_at")
        Instant createdAt,
        @NotNull
        @Email
        @SerializedName("email")
        String email,
        @NotNull
        @SerializedName("avatar_url")
        String avatarUrl
) {}