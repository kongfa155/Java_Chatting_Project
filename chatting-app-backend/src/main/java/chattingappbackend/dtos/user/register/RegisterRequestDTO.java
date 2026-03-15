package chattingappbackend.dtos.user.register;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public record RegisterRequestDTO(
        @NotBlank(message = "Username cannot be blank")
        @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
        @Pattern(regexp = "^[a-zA-Z0-9._]+$", message = "Username can only contain letters, numbers, dots, and underscores")
        @JsonProperty("username")
        String username,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
        @JsonProperty("password")
        String password,

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        @JsonProperty("email")
        String email,

        @NotBlank(message = "Display name cannot be blank")
        @Size(max = 100, message = "Display name must not exceed 100 characters")
        @JsonProperty("display_name")
        String displayName,

        @NotNull(message = "Gender is required")
        @JsonProperty("gender")
        Boolean gender,

        @Size(max = 255, message = "Avatar URL is too long")
        @JsonProperty("avatar_url")
        String avatarUrl
) {}