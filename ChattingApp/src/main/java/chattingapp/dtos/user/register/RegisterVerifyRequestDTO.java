package chattingapp.dtos.user.register;

import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterVerifyRequestDTO(
        @SerializedName("username")
        @NotBlank(message = "Username is required")
        String username,
        
        @SerializedName("otp")
        @NotBlank(message = "OTP is required")
        @Size(min = 6, max = 6, message = "OTP must be exactly 6 characters")
        String otp
) {}