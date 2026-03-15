package chattingappbackend.dtos.user.forgotpassword;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ForgotPasswordOTPRequestDTO(
        @NotBlank(message = "Username cannot be blank")
        @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
        @JsonProperty("username")
        String username
) {}