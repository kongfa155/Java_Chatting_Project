package chattingappbackend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangeProfileRequestDTO {
    @JsonProperty("password")
    private String password;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("gender")
    private boolean gender;
    @JsonProperty("avatar_url")
    private String avatarURL;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public ChangeProfileRequestDTO() {
    }

    public ChangeProfileRequestDTO(String password, String displayName, boolean gender, String avatarURL) {
        this.password = password;
        this.displayName = displayName;
        this.gender = gender;
        this.avatarURL = avatarURL;
    }
}
