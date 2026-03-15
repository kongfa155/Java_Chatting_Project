package chattingappbackend.dtos.user.changeemail;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangeEmailRequestDTO(
        @NotBlank(message = "New email is required")
        @Email(message = "Invalid email format")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        @JsonProperty("new_email")
        String newEmail,

        @NotBlank(message = "OTP is required")
        @Size(min = 6, max = 6, message = "OTP must be exactly 6 characters")
        @JsonProperty("otp")
        String otp
) {}