package chattingapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterRequestDTO {
    @JsonProperty("gender")
    private Boolean gender;
    @JsonProperty("username")
    private String username;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("avatar_url")
    private String avatarUrl;
    @JsonProperty("password")
    private String password;

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RegisterRequestDTO(Boolean gender, String username, String phoneNumber, String displayName, String avatarUrl, String password) {
        this.gender = gender;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.displayName = displayName;
        this.avatarUrl = avatarUrl;
        this.password = password;
    }

    public RegisterRequestDTO() {
    }
}
