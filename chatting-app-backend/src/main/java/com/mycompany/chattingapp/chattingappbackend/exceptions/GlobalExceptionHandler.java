package com.mycompany.chattingapp.chattingappbackend.exceptions;

import com.mycompany.chattingapp.chattingappbackend.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException ex){
        ApiResponse<Void> response = new ApiResponse<>("error", ex.getMessage(), "SYSTEM_ERROR", null);
        return ResponseEntity.badRequest().body(response);

    }
}
