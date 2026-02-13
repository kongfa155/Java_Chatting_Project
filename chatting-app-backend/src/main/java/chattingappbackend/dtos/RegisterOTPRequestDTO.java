package chattingappbackend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterOTPRequestDTO {
    @JsonProperty("username")
    private String username;

    public RegisterOTPRequestDTO(String username) {
        this.username = username;
    }

    public RegisterOTPRequestDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
