/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chattingapp.config;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;

/**
 *
 * @author CP
 */
public class ThemeManager {
    //Hàm setup giao diện trắng với FlatLaf
    public static void setupLightTheme() {
        try {
            //Sử dụng FlatLaf để làm cho giao diện hiện đại hơn
            FlatLightLaf.setup();
            
            //Thiết kế màu và bo tròn các nút
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("TextComponent.arc", 10);
            //Nút ẩn hiện mật khẩu
            UIManager.put("PasswordField.showRevealButton", true);

            UIManager.put("Button.background", new java.awt.Color(0, 104, 255));
            UIManager.put("Button.foreground", java.awt.Color.WHITE);
        } catch (Exception ex){
            System.err.println("Lỗi khởi tạo FlatLaf");
        }
    }
    
}
