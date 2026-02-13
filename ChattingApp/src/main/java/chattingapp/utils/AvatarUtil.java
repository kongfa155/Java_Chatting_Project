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
import com.formdev.flatlaf.util.UIScale;

/**
 *
 * @author CP
 */



public class AvatarUtil {

    public static ImageIcon loadAvatar(String pathOrUrl, int size, boolean online) {
        try {
            int scaledSize = UIScale.scale(size);

            BufferedImage original;
            if (pathOrUrl.startsWith("http")) {
                original = ImageIO.read(new URL(pathOrUrl));
            } else {
                original = ImageIO.read(new File(pathOrUrl));
            }

            if (original == null)
                throw new RuntimeException("Image read failed");

            // 1️⃣ Crop vuông từ center
            BufferedImage square = cropToSquare(original);
            int originalSize = square.getWidth();

            // 2️⃣ Supersample nếu ảnh đủ lớn
            int superSize = Math.min(originalSize, scaledSize * 2);
            BufferedImage large = resize(square, superSize, superSize);

            // 3️⃣ Cắt tròn
            BufferedImage circle = makeCircle(large);

            // 4️⃣ Resize về kích thước chuẩn
            BufferedImage finalImg = resize(circle, scaledSize, scaledSize);

            // 5️⃣ Thêm border trắng (Light theme nhìn đẹp hơn)
            finalImg = addBorder(finalImg, UIScale.scale(2));

            // 6️⃣ Thêm online indicator nếu cần
            if (online) {
                finalImg = addOnlineIndicator(finalImg);
            }

            // 7️⃣ Thêm shadow nhẹ
            finalImg = addShadow(finalImg, UIScale.scale(4));

            return new ImageIcon(finalImg);

        } catch (Exception e) {
            e.printStackTrace();
            return createDefaultAvatar(size);
        }
    }

    // Crop center thành hình vuông
    private static BufferedImage cropToSquare(BufferedImage img) {
        int size = Math.min(img.getWidth(), img.getHeight());
        int x = (img.getWidth() - size) / 2;
        int y = (img.getHeight() - size) / 2;
        return img.getSubimage(x, y, size, size);
    }

    // Resize chất lượng cao
    private static BufferedImage resize(BufferedImage img, int w, int h) {
        BufferedImage resized = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resized.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

        g2.drawImage(img, 0, 0, w, h, null);
        g2.dispose();
        return resized;
    }

    // Cắt tròn mượt
    private static BufferedImage makeCircle(BufferedImage img) {
        int size = img.getWidth();
        BufferedImage circle = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = circle.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.fillOval(0, 0, size, size);
        g2.setComposite(AlphaComposite.SrcIn);
        g2.drawImage(img, 0, 0, null);

        g2.dispose();
        return circle;
    }

    // Thêm border trắng (Light theme)
    private static BufferedImage addBorder(BufferedImage img, int thickness) {
        int size = img.getWidth();
        Graphics2D g2 = img.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(thickness));
        g2.drawOval(thickness / 2, thickness / 2,
                size - thickness, size - thickness);

        g2.dispose();
        return img;
    }

    // Thêm shadow nhẹ tinh tế
    private static BufferedImage addShadow(BufferedImage img, int shadowSize) {
        int size = img.getWidth();
        int total = size + shadowSize;

        BufferedImage shadowImg = new BufferedImage(total, total, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = shadowImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Shadow mềm nhẹ (Light theme nên rất nhẹ)
        g2.setColor(new Color(0, 0, 0, 40));
        g2.fillOval(shadowSize, shadowSize, size, size);

        g2.drawImage(img, 0, 0, null);

        g2.dispose();
        return shadowImg;
    }

    // Online indicator
    private static BufferedImage addOnlineIndicator(BufferedImage img) {
        int size = img.getWidth();
        Graphics2D g2 = img.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int dotSize = size / 4;
        int x = size - dotSize;
        int y = size - dotSize;

        g2.setColor(new Color(0, 200, 0));
        g2.fillOval(x, y, dotSize, dotSize);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(x, y, dotSize, dotSize);

        g2.dispose();
        return img;
    }

    // Avatar mặc định
    private static ImageIcon createDefaultAvatar(int size) {
        int scaledSize = UIScale.scale(size);
        BufferedImage img = new BufferedImage(scaledSize, scaledSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(new Color(230, 230, 230));
        g.fillOval(0, 0, scaledSize, scaledSize);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, scaledSize / 2));
        FontMetrics fm = g.getFontMetrics();
        String text = "?";

        int x = (scaledSize - fm.stringWidth(text)) / 2;
        int y = (scaledSize - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(text, x, y);

        g.dispose();
        return new ImageIcon(img);
    }
}
