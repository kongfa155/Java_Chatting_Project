package chattingapp.dtos.user.changepassword;

import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequestDTO(
        @SerializedName("old_password")
        @NotBlank(message = "Old password cannot be blank")
        String oldPassword,
        
        @SerializedName("new_password")
        @NotBlank(message = "New password cannot be blank")
        @Size(min = 6, max = 100, message = "New password must be between 6 and 100 characters")
        String newPassword
) {}