package chattingapp.dtos.user.changeprofile;


import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChangeProfileResponseDTO(
        @SerializedName("display_name")
        @NotBlank(message = "Display name cannot be blank")
        @Size(max = 100, message = "Display name must not exceed 100 characters")
        String displayName,

        @SerializedName("gender")
        @NotNull(message = "Gender is required")
        Boolean gender,

        @Size(max = 255, message = "Avatar URL is too long")
        @SerializedName("avatar_url")
        String avatarUrl
) {}