/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chattingapp.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
/**
 *
 * @author CP
 */


public class AvatarUtil {

    public static ImageIcon loadAvatar(String pathOrUrl, int size) {
    try {
        BufferedImage original;

        System.out.println("Loading avatar: " + pathOrUrl);

        if (pathOrUrl.startsWith("http")) {
            URL url = new URL(pathOrUrl);
            original = ImageIO.read(url);
        } else {
            original = ImageIO.read(new File(pathOrUrl));
        }

        System.out.println("Image object = " + original);

        if (original == null) {
            System.out.println("❌ ImageIO.read() returned NULL");
            throw new RuntimeException("Image read failed");
        }

        BufferedImage resized = resize(original, size, size);
        BufferedImage circle = makeCircle(resized);

        System.out.println("✅ Avatar loaded successfully");
        return new ImageIcon(circle);

    } catch (Exception e) {
        System.out.println("❌ Load avatar failed:");
        e.printStackTrace();   // QUAN TRỌNG
        return createDefaultAvatar(size);
    }
}
//    // Load avatar từ URL hoặc local path
//    public static ImageIcon loadAvatar(String pathOrUrl, int size) {
//        try {
//            BufferedImage original;
//
//            if (pathOrUrl.startsWith("http")) {
//                original = ImageIO.read(new URL(pathOrUrl));
//            } else {
//                original = ImageIO.read(new File(pathOrUrl));
//            }
//
//            BufferedImage resized = resize(original, size, size);
//            BufferedImage circle = makeCircle(resized);
//
//            return new ImageIcon(circle);
//        } catch (Exception e) {
//            // fallback avatar
//            return createDefaultAvatar(size);
//        }
//    }

    // Resize ảnh
    private static BufferedImage resize(BufferedImage img, int w, int h) {
        Image tmp = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resized.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(tmp, 0, 0, null);
        g2.dispose();
        return resized;
    }

    // Crop tròn
    private static BufferedImage makeCircle(BufferedImage img) {
        int size = Math.min(img.getWidth(), img.getHeight());
        BufferedImage circle = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = circle.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Ellipse2D.Double circleShape = new Ellipse2D.Double(0, 0, size, size);
        g2.setClip(circleShape);
        g2.drawImage(img, 0, 0, size, size, null);
        g2.dispose();

        return circle;
    }

    // Avatar mặc định
    private static ImageIcon createDefaultAvatar(int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();

        g.setColor(new Color(220, 220, 220));
        g.fillOval(0, 0, size, size);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, size / 2));
        FontMetrics fm = g.getFontMetrics();
        String text = "?";
        int x = (size - fm.stringWidth(text)) / 2;
        int y = (size - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(text, x, y);

        g.dispose();
        return new ImageIcon(img);
    }
}

