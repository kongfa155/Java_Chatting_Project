package chattingapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequetDTO {
    @JsonProperty("username")
    private String username;
    private String password;

    public LoginRequetDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginRequetDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
