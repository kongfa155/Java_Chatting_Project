package chattingappbackend.dtos.user.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequetDTO(
        @NotBlank(message = "Username is required")
        @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
        @JsonProperty("username")
        String username,

        @NotBlank(message = "Password is required")
        @JsonProperty("password")
        String password
) {}