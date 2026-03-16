package chattingapp.dtos.user.register;



import chattingapp.models.UserStatus;
import com.google.gson.annotations.SerializedName;
import java.time.Instant;

public record RegisterVerifyResponseDTO(

        @SerializedName("access_token")
        String accessToken,

        @SerializedName("expires_in")
        int expiresIn,

        @SerializedName("userId")
        String userId,

        @SerializedName("username")
        String username,

        @SerializedName("display_name")
        String displayName,
        @SerializedName("gender")
        boolean gender,

        @SerializedName("status")
        UserStatus status,

        @SerializedName("created_at")
        Instant createdAt
) {
        public RegisterVerifyResponseDTO(String userId, String username, String displayName, boolean gender, UserStatus status, Instant createdAt) {
                this(null, 0, userId, username, displayName, gender, status, createdAt);
        }
}