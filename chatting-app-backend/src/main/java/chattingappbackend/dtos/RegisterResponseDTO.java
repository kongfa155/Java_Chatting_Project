package chattingappbackend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import chattingappbackend.models.UserStatus;

public class RegisterResponseDTO {
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("status")
    private UserStatus status;

    public RegisterResponseDTO(String displayName, UserStatus status) {
        this.displayName = displayName;
        this.status = status;
    }

    public RegisterResponseDTO() {
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
