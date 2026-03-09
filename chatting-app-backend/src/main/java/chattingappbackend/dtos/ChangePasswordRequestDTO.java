package chattingappbackend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangePasswordRequestDTO {

    @JsonProperty("old_password")
    private String oldPassword;
    @JsonProperty("new_password")
    private String newPassword;


    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public ChangePasswordRequestDTO() {
    }

    public ChangePasswordRequestDTO(String oldPassword, String newPassword) {

        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
