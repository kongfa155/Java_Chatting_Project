package chattingappbackend.dtos.user.register;

import chattingappbackend.models.UserStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterResponseDTO(
        @NotBlank(message = "Display name cannot be blank")
        @Size(max = 100, message = "Display name must not exceed 100 characters")
        @JsonProperty("display_name")
        String displayName,

        @NotNull(message = "User status is required")
        @JsonProperty("status")
        UserStatus status
) {}