package chattingappbackend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ForgotPasswordOTPRequestDTO {
    @JsonProperty("username")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ForgotPasswordOTPRequestDTO() {
    }

    public ForgotPasswordOTPRequestDTO(String username) {
        this.username = username;
    }
}
