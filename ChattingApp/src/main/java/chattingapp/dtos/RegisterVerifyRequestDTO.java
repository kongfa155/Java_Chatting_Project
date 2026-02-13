package chattingapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterVerifyRequestDTO {
    @JsonProperty("username")
    private String username;
    @JsonProperty("otp")
    private String otp;

    public RegisterVerifyRequestDTO(String username, String otp) {
        this.username = username;
        this.otp = otp;
    }

    public RegisterVerifyRequestDTO() {
    }

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
}

