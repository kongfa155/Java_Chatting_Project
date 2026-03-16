package chattingapp.services;

import chattingapp.config.GsonConfig;
import chattingapp.exceptions.AppException;
import chattingapp.responses.ApiResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

/**
 * BaseService - Lớp cơ sở xử lý logic HTTP và Mapping dữ liệu.
 * Đảm bảo các tiêu chuẩn về an toàn dữ liệu và xử lý lỗi tập trung.
 */
public class BaseService {

    // Khởi tạo GSON từ Config đã đăng ký TypeAdapters (xử lý Instant/LocalDateTime)
    protected static final Gson GSON = GsonConfig.getGson();

    /**
     * Tạo POST Request với Body là JSON.
     */
    protected HttpRequest createPostRequest(String endpoint, Object dto) {
        return HttpRequest.newBuilder()
                .uri(URI.create(ApiClient.getBaseUrl() + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(dto)))
                .build();
    }

    /**
     * Phương thức xử lý lỗi tập trung (Private).
     * Bóc tách mã lỗi (errorCode) từ Backend để UI có thể xử lý logic (ví dụ: mở OTPFrame).
     */
    private <T> T handleError(HttpResponse<String> response) {
        String body = response.body();
        int statusCode = response.statusCode();

        try {
            // Parse body về ApiResponse để lấy errorCode và message
            ApiResponse<?> errorResponse = GSON.fromJson(body, ApiResponse.class);

            if (errorResponse != null) {
                // Ưu tiên lấy errorCode từ Backend, nếu không có dùng mã HTTP
                String code = (errorResponse.getErrorCode() != null) 
                              ? errorResponse.getErrorCode() 
                              : "HTTP_" + statusCode;
                
                String msg = (errorResponse.getMessage() != null) 
                             ? errorResponse.getMessage() 
                             : "Lỗi không xác định từ hệ thống.";

                throw new AppException(code, msg);
            }
        } catch (Exception e) {
            // Nếu đã là AppException thì ném tiếp
            if (e instanceof AppException) throw (AppException) e;
            
            // Lỗi parse JSON hoặc lỗi kết nối
            throw new AppException("SERVER_ERROR", "Không thể kết nối đến máy chủ (Status: " + statusCode + ")");
        }
        
        throw new AppException("UNKNOWN_ERROR", "Lỗi hệ thống không xác định.");
    }

    /**
     * Xử lý Response cho đối tượng đơn.
     */
    protected <T> T handleResponse(HttpResponse<String> response, Class<T> clazz) {
        int statusCode = response.statusCode();

        // Kiểm tra thành công (2xx)
        if (statusCode >= 200 && statusCode < 300) {
            if (clazz == Void.class) return null;

            String body = response.body();
            // Sử dụng TypeToken để xử lý Generic ApiResponse<T>
            Type type = TypeToken.getParameterized(ApiResponse.class, clazz).getType();
            ApiResponse<T> apiResponse = GSON.fromJson(body, type);
            
            return (apiResponse != null) ? apiResponse.getData() : null;
        }

        // Xử lý lỗi nếu không phải 2xx
        return handleError(response);
    }

    /**
     * Xử lý Response cho danh sách đối tượng (List<T>).
     */
    protected <T> List<T> handleResponseList(HttpResponse<String> response, Class<T> clazz) {
        int statusCode = response.statusCode();

        if (statusCode >= 200 && statusCode < 300) {
            String body = response.body();
            
            // Tạo Type cho List<T> sau đó tạo Type cho ApiResponse<List<T>>
            Type listType = TypeToken.getParameterized(List.class, clazz).getType();
            Type apiResponseType = TypeToken.getParameterized(ApiResponse.class, listType).getType();

            ApiResponse<List<T>> apiResponse = GSON.fromJson(body, apiResponseType);
            
            // Enterprise Standard: Luôn trả về List rỗng thay vì null để tránh NullPointerException ở UI
            if (apiResponse == null || apiResponse.getData() == null) {
                return Collections.emptyList();
            }
            return apiResponse.getData();
        }

        return handleError(response);
    }
}