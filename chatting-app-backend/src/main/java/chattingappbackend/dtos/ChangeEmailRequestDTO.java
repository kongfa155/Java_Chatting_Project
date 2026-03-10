package chattingappbackend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangeEmailRequestDTO {
    @JsonProperty("new_email")
    private String newEmail;
    @JsonProperty("otp")
    private String otp;

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public ChangeEmailRequestDTO() {
    }

    public ChangeEmailRequestDTO(String newEmail, String otp) {
        this.newEmail = newEmail;
        this.otp = otp;
    }
}
