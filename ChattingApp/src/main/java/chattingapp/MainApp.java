/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chattingapp;
import chattingapp.config.ThemeManager;
import chattingapp.ui.LoginFrame;
/**
 *
 * @author CP
 */
public class MainApp {
    public static void main(String [] args){
        ThemeManager.setupLightTheme();
        
        //Đưa LoginFrame vào luồng UI
        //InvokeLater sẽ lấy Runnable chạy khi luồng rảnh
        java.awt.EventQueue.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
