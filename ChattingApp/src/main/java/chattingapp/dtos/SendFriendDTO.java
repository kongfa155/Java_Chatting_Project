/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chattingapp.dtos;

/**
 *
 * @author CP
 */
public class SendFriendDTO {
    private String email;
    public SendFriendDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
