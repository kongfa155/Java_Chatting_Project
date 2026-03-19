package chattingapp.utils;

import chattingapp.exceptions.AppException;
import java.awt.Component;
import java.util.concurrent.CompletionException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * GlobalErrorHandler - Hệ thống xử lý lỗi tập trung tiêu chuẩn Enterprise.
 * Mapping toàn bộ lỗi từ các Service: User, Friendship, Message, Notification.
 */
public class GlobalErrorHandler {

    public static void handle(Component parent, Throwable throwable) {
        Throwable cause = (throwable instanceof CompletionException)
                ? throwable.getCause() : throwable;

        String message;
        String title;

        if (cause instanceof AppException appEx) {
            title = "Thông báo hệ thống";
            message = mapErrorCodeToMessage(appEx.getErrorCode(), appEx.getMessage());
        } else if (cause instanceof java.net.ConnectException || cause instanceof java.net.UnknownHostException) {
            message = "Không thể kết nối tới máy chủ. Vui lòng kiểm tra kết nối mạng của bạn.";
            title = "Lỗi kết nối";
        } else if (cause instanceof java.util.concurrent.TimeoutException) {
            message = "Thời gian phản hồi quá lâu. Vui lòng thử lại sau.";
            title = "Lỗi phản hồi";
        } else {
            title = "Thông báo hệ thống";
            message = "Đã xảy ra lỗi không xác định: " + cause.getMessage();
        }

        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
        });
    }

    private static String mapErrorCodeToMessage(String errorCode, String serverMessage) {
        if (errorCode == null) return serverMessage;

        return switch (errorCode) {
            // --- USER & AUTH ERRORS ---
            case "DTO_INVALID", "SYSTEM_ERROR" -> "Dữ liệu nhập vào không hợp lệ hoặc sai định dạng.";
            case "INVALID_OTP" -> "Mã xác thực (OTP) không chính xác hoặc đã hết hạn.";
            case "INVALID_CREDENTIALS" -> "Tên đăng nhập hoặc mật khẩu không chính xác.";
            case "USERNAME_EXISTS" -> "Tên đăng nhập này đã có người sử dụng.";
            case "EMAIL_EXISTS" -> "Email này đã được đăng ký bởi một tài khoản khác.";
            case "ACCOUNT_IS_NOT_ACTIVATED" -> "Tài khoản của bạn chưa được kích hoạt.";
            case "USER_NOT_FOUND", "USER_NOT_EXISTS" -> "Không tìm thấy thông tin người dùng yêu cầu.";
            case "WRONG_OLD_PASSWORD" -> "Mật khẩu cũ không chính xác.";

            // --- FRIENDSHIP ERRORS (FriendshipService) ---
            case "INVALID_ACTION" -> "Bạn không thể thực hiện thao tác này (ví dụ: tự kết bạn).";
            case "ALREADY_FRIENDS" -> "Hai bạn đã là bạn bè của nhau.";
            case "REQUEST_ALREADY_SENT" -> "Lời mời kết bạn này đã được gửi trước đó và đang chờ xử lý.";
            case "NOT_FOUND" -> "Yêu cầu kết bạn không tồn tại hoặc đã bị hủy.";
            case "FORBIDDEN" -> "Bạn không có quyền thực hiện thao tác xác nhận này.";

            // --- MESSAGE ERRORS (MessageService) ---
            case "INVALID_RECEIVER" -> "Người nhận tin nhắn không hợp lệ.";
            case "EMPTY_MESSAGE" -> "Nội dung tin nhắn không được để trống.";
            case "MESSAGE_NOT_FOUND" -> "Tin nhắn không tồn tại hoặc đã bị xóa.";

            // --- DEFAULT CASE ---
            default -> (serverMessage != null && !serverMessage.isBlank())
                    ? serverMessage
                    : "Lỗi nghiệp vụ hệ thống (Mã: " + errorCode + ")";
        };
    }
}