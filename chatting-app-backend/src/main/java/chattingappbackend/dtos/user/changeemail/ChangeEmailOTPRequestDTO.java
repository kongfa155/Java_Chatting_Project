package chattingappbackend.dtos.user.changeemail;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangeEmailOTPRequestDTO(
        @NotBlank(message = "Password is required")
        @JsonProperty("password")
        String password,

        @NotBlank(message = "New email is required")
        @Email(message = "Invalid email format")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        @JsonProperty("new_email")
        String newEmail
) {}