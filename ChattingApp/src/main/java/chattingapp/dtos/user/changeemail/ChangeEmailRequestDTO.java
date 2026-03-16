package chattingapp.dtos.user.changeemail;

import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangeEmailRequestDTO(
        @NotBlank(message = "New email is required")
        @Email(message = "Invalid email format")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        @SerializedName("new_email")
        String newEmail,
        
        @SerializedName("otp")
        @NotBlank(message = "OTP is required")
        @Size(min = 6, max = 6, message = "OTP must be exactly 6 characters")
        String otp
) {}