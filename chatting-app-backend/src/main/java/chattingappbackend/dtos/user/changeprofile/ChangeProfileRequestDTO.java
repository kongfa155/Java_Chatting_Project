package chattingappbackend.dtos.user.changeprofile;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChangeProfileRequestDTO(
        @NotBlank(message = "Password is required to verify changes")
        @JsonProperty("password")
        String password,

        @NotBlank(message = "Display name cannot be blank")
        @Size(max = 100, message = "Display name must not exceed 100 characters")
        @JsonProperty("display_name")
        String displayName,

        @NotNull(message = "Gender is required")
        @JsonProperty("gender")
        Boolean gender, // Dùng Boolean (Object) thay vì boolean (primitive) để @NotNull hoạt động chính xác

        @Size(max = 255, message = "Avatar URL is too long")
        @JsonProperty("avatar_url")
        String avatarUrl
) {}