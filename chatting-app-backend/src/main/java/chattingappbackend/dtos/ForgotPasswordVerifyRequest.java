package chattingappbackend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ForgotPasswordVerifyRequest {
    @JsonProperty("username")
    private String username;
    @JsonProperty("otp")
    private String otp;
    @JsonProperty("new_password")
    private String newPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public ForgotPasswordVerifyRequest() {
    }

    public ForgotPasswordVerifyRequest(String username, String otp, String newPassword) {
        this.username = username;
        this.otp = otp;
        this.newPassword = newPassword;
    }
}
