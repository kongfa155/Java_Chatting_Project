package chattingapp.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AvatarUtil: Xử lý ảnh đại diện. Luồng thực thi: Load
 * -> Crop Square -> Resize -> Make Circle -> Draw Status -> Cache
 */
public class AvatarUtil {

    // Cache lưu trữ để tránh việc xử lý lại cùng một ảnh nhiều lần
    private static final Map<String, ImageIcon> CACHE = new ConcurrentHashMap<>();

    // --- 1. ENTRY POINT (ĐIỂM VÀO CHÍNH) ---
    /**
     * Nạp avatar từ đường dẫn (file/url), xử lý bo tròn và trạng thái online.
     */
    public static ImageIcon loadAvatar(String pathOrUrl, int size, boolean online) {
        if (pathOrUrl == null || pathOrUrl.isEmpty()) {
            return createDefaultAvatar(size);
        }

        String cacheKey = pathOrUrl + "_" + size + "_" + online;
        if (CACHE.containsKey(cacheKey)) {
            return CACHE.get(cacheKey);
        }

        try {
            // Bước 1: Tải ảnh gốc
            BufferedImage img = loadImage(pathOrUrl);
            if (img == null) {
                return createDefaultAvatar(size);
            }

            // Bước 2: Xử lý qua pipeline
            BufferedImage processed = processAvatarPipeline(img, size, online);

            // Bước 3: Đưa vào cache và trả về kết quả
            ImageIcon icon = new ImageIcon(processed);
            CACHE.put(cacheKey, icon);
            return icon;

        } catch (Exception e) {
            System.err.println("Error loading avatar: " + pathOrUrl + " | " + e.getMessage());
            return createDefaultAvatar(size);
        }
    }

    // --- 2. LOAD & PIPELINE ---
    /**
     * Tải ảnh lên
     */
    private static BufferedImage loadImage(String path) throws Exception {
        if (path.startsWith("http")) {
            URI uri = new URI(path);
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            try (var is = conn.getInputStream()) {
                return ImageIO.read(is);
            }
        } else {
            File file = new File(path);
            if (!file.exists()) {
                return null;
            }
            return ImageIO.read(file);
        }
    }

    /**
     * Cắt thành vuông, xong resize rồi mới cắt về hình tròn
     */
    private static BufferedImage processAvatarPipeline(BufferedImage img, int size, boolean online) {
        BufferedImage square = cropToSquare(img);
        BufferedImage resized = resizeImage(square, size);
        BufferedImage circle = convertToCircle(resized);

        if (online) {
            drawOnlineStatus(circle);
        }
        return circle;
    }

    // --- 3. CÁC BƯỚC XỬ LÝ CHI TIẾT (IMAGE MANIPULATION) ---
    /**
     * Cắt vuông từ tâm
     */
    private static BufferedImage cropToSquare(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        int size = Math.min(width, height);

        int x = (width - size) / 2;
        int y = (height - size) / 2;

        return img.getSubimage(x, y, size, size);
    }

    /**
     * Thay đổi kích thước ảnh về kích thước yêu cầu
     */
    private static BufferedImage resizeImage(BufferedImage img, int size) {
        BufferedImage out = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();

        applyQualityHints(g);
        g.drawImage(img, 0, 0, size, size, null);
        g.dispose();

        return out;
    }

    /**
     * Cắt ảnh bằng hình tròn sử dụng clipping
     */
    private static BufferedImage convertToCircle(BufferedImage img) {
        int size = img.getWidth();
        BufferedImage circle = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = circle.createGraphics();

        applyQualityHints(g);
        // Tạo vùng cắt hình elip (tròn)
        g.setClip(new Ellipse2D.Float(0, 0, size, size));
        g.drawImage(img, 0, 0, null);

        g.dispose();
        return circle;
    }

    /**
     * Vẽ chấm màu xanh hiển thị online
     * Online.
     */
    private static void drawOnlineStatus(BufferedImage img) {
        Graphics2D g = img.createGraphics();
        applyQualityHints(g);

        int size = img.getWidth();
        int dotSize = size / 4;
        int position = size - dotSize - (size / 20); // Cách lề một chút cho đẹp

        // Vẽ viền trắng cho chấm online (tạo hiệu ứng tách biệt với avatar)
        g.setColor(Color.WHITE);
        g.fillOval(position - 1, position - 1, dotSize + 2, dotSize + 2);

        // Vẽ chấm xanh
        g.setColor(new Color(35, 165, 90)); // Màu xanh Discord-style
        g.fillOval(position, position, dotSize, dotSize);

        g.dispose();
    }

    // --- 4. UTILS & FALLBACK ---
    /**
     * Sử dụng khi cần ảnh nét
     */
    private static void applyQualityHints(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }

    /**
     * Avatar mặc định nếu lỗi không load được
     */
    private static ImageIcon createDefaultAvatar(int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();

        applyQualityHints(g);

        // Vẽ nền xám
        g.setColor(new Color(200, 200, 200));
        g.fillOval(0, 0, size, size);

        // Vẽ chữ "?" chính giữa
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, size / 2));

        FontMetrics fm = g.getFontMetrics();
        String text = "?";
        int x = (size - fm.stringWidth(text)) / 2;
        int y = ((size - fm.getHeight()) / 2) + fm.getAscent();

        g.drawString(text, x, y);
        g.dispose();

        return new ImageIcon(img);
    }
}
