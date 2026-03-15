package chattingappbackend.exceptions;

import chattingappbackend.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException ex){
        ApiResponse<Void> response = new ApiResponse<>("error", "Invalid request", "SYSTEM_ERROR", null);
        return ResponseEntity.badRequest().body(response);

    }
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Void>> handleAppException(AppException ex){
        ApiResponse<Void> response = new ApiResponse<>("error", ex.getMessage(), ex.getErrorCode(), null);
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ApiResponse<Map<String, String>> response = new ApiResponse<>();
        response.setStatus("ERROR");
        response.setMessage("DTO is invalid");
        response.setData(errors);

        return ResponseEntity.badRequest().body(response);
    }
}
