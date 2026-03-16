package chattingapp.dtos.user.changeprofile;

import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChangeProfileRequestDTO(
        @NotBlank(message = "Password is required to verify changes")
        @SerializedName("password")
        String password,

        @NotBlank(message = "Display name cannot be blank")
        @Size(max = 100, message = "Display name must not exceed 100 characters")
        @SerializedName("display_name")
        String displayName,
        
        @SerializedName("gender")
        @NotNull(message = "Gender is required")
        Boolean gender, // Dùng Boolean (Object) thay vì boolean (primitive) để @NotNull hoạt động chính xác

        @Size(max = 255, message = "Avatar URL is too long")
        @SerializedName("avatar_url")       
        String avatarUrl
) {}