package chattingappbackend.dtos.user.register;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterVerifyRequestDTO(
        @NotBlank(message = "Username is required")
        @JsonProperty("username")
        String username,

        @NotBlank(message = "OTP is required")
        @Size(min = 6, max = 6, message = "OTP must be exactly 6 characters")
        @JsonProperty("otp")
        String otp
) {}