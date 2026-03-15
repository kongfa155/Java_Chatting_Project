package chattingappbackend.dtos.user.changepassword;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequestDTO(
        @NotBlank(message = "Old password cannot be blank")
        @JsonProperty("old_password")
        String oldPassword,

        @NotBlank(message = "New password cannot be blank")
        @Size(min = 6, max = 100, message = "New password must be between 6 and 100 characters")
        @JsonProperty("new_password")
        String newPassword
) {}