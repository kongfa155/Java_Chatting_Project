package chattingappbackend.dtos.user.forgotpassword;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ForgotPasswordVerifyRequest(
        @NotBlank(message = "Username cannot be blank")
        @JsonProperty("username")
        String username,

        @NotBlank(message = "OTP cannot be blank")
        @Size(min = 6, max = 6, message = "OTP must be exactly 6 characters")
        @JsonProperty("otp")
        String otp,

        @NotBlank(message = "New password cannot be blank")
        @Size(min = 6, max = 100, message = "New password must be between 6 and 100 characters")
        @JsonProperty("new_password")
        String newPassword
) {}