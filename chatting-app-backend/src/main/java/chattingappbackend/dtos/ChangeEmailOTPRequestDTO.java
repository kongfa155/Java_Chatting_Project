package chattingappbackend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangeEmailOTPRequestDTO {
    @JsonProperty("password")
    private String password;
    @JsonProperty("new_email")
    private String newEmail;

    public ChangeEmailOTPRequestDTO(String password, String newEmail) {
        this.password = password;
        this.newEmail = newEmail;
    }

    public ChangeEmailOTPRequestDTO() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }
}
