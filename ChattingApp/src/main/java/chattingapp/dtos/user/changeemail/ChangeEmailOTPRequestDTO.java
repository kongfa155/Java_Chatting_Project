package chattingapp.dtos.user.changeemail;

import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangeEmailOTPRequestDTO(
        @SerializedName("password")
        @NotBlank(message = "Password is required")
        String password,
        @SerializedName("new_email")
        @NotBlank(message = "New email is required")
        @Email(message = "Invalid email format")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        String newEmail
) {}