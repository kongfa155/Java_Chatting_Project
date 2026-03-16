package chattingapp.dtos.user.register;
import chattingapp.models.UserStatus;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterResponseDTO(
        @SerializedName("display_name")
        @NotBlank(message = "Display name cannot be blank")
        @Size(max = 100, message = "Display name must not exceed 100 characters")
        String displayName,
        
        @SerializedName("status")
        @NotNull(message = "User status is required")
        UserStatus status
) {}