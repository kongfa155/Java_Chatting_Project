package chattingapp.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String status;
    private String message;
    private String errorCode;
    private T data;


    public static <T> ApiResponse<T> success(String message, T data){
        return new ApiResponse<>("success", message, null, data);
    }
    public static <T> ApiResponse<T> error(String errorCode, String message){
        return new ApiResponse<>("error", message, errorCode, null);
    }


    public ApiResponse(String status, String message, String errorCode, T data) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
        this.data = data;
    }

    public ApiResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
