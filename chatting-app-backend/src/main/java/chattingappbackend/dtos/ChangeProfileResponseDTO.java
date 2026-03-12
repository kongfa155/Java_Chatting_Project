package chattingappbackend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangeProfileResponseDTO {
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("gender")
    private boolean gender;
    @JsonProperty("avatar_url")
    private String avatarURL;




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

    public ChangeProfileResponseDTO() {
    }

    public ChangeProfileResponseDTO(String displayName, boolean gender, String avatarURL) {

        this.displayName = displayName;
        this.gender = gender;
        this.avatarURL = avatarURL;
    }
}
