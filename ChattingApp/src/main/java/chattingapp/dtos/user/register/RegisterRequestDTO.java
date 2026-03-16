package chattingapp.dtos.user.register;

import jakarta.validation.constraints.*;
import com.google.gson.annotations.SerializedName;

public record RegisterRequestDTO(
        @NotBlank(message = "Username cannot be blank")
        @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
        @Pattern(regexp = "^[a-zA-Z0-9._]+$", message = "Username can only contain letters, numbers, dots, and underscores")
        @SerializedName("username")
        String username,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
        @SerializedName("password")
        String password,

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        @SerializedName("email")
        String email,

        @NotBlank(message = "Display name cannot be blank")
        @Size(max = 100, message = "Display name must not exceed 100 characters")
        // LƯU Ý: Nếu Backend của bạn dùng Snake_Case, hãy đổi dòng dưới thành @SerializedName("display_name")
        @SerializedName("display_name") 
        String displayName,

        @NotNull(message = "Gender is required")
        @SerializedName("gender")
        Boolean gender,

        @Size(max = 255, message = "Avatar URL is too long")
        // LƯU Ý: Nếu Backend của bạn dùng Snake_Case, hãy đổi dòng dưới thành @SerializedName("avatar_url")
        @SerializedName("avatar_url") 
        String avatarUrl
) {}