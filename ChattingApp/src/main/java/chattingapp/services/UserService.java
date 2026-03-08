package chattingapp.services;

import chattingapp.dtos.*;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class UserService extends BaseService {

    public CompletableFuture<RegisterResponseDTO> register(RegisterRequestDTO dto) {
        HttpRequest request = createPostRequest("/users/register", dto);
        return ApiClient.getClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> handleResponse(response, RegisterResponseDTO.class));
    }

    public CompletableFuture<RegisterVerifyResponseDTO> verifyRegister(RegisterVerifyRequestDTO dto) {
        HttpRequest request = createPostRequest("/users/verify", dto);
        return ApiClient.getClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> handleResponse(response, RegisterVerifyResponseDTO.class));
    }

    public CompletableFuture<Void> getRegisterOTP(RegisterOTPRequestDTO dto) {
        HttpRequest request = createPostRequest("/users/get-register-otp", dto);
        return ApiClient.getClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> handleResponse(response, Void.class));
    }

    // --- Viết thêm hàm login theo mẫu của bạn ---
    public CompletableFuture<LoginResponseDTO> login(LoginRequetDTO dto) {
        HttpRequest request = createPostRequest("/users/login", dto);
        return ApiClient.getClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> handleResponse(response, LoginResponseDTO.class));
    }
}