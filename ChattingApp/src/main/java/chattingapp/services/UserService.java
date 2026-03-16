package chattingapp.services;

import chattingapp.dtos.*;
import chattingapp.dtos.user.changeemail.ChangeEmailOTPRequestDTO;
import chattingapp.dtos.user.changeemail.ChangeEmailRequestDTO;
import chattingapp.dtos.user.changepassword.ChangePasswordRequestDTO;
import chattingapp.dtos.user.changeprofile.ChangeProfileRequestDTO;
import chattingapp.dtos.user.forgotpassword.ForgotPasswordOTPRequestDTO;
import chattingapp.dtos.user.forgotpassword.ForgotPasswordVerifyRequest;
import chattingapp.dtos.user.login.LoginRequetDTO;
import chattingapp.dtos.user.login.LoginResponseDTO;
import chattingapp.dtos.user.register.RegisterOTPRequestDTO;
import chattingapp.dtos.user.register.RegisterRequestDTO;
import chattingapp.dtos.user.register.RegisterResponseDTO;
import chattingapp.dtos.user.register.RegisterVerifyRequestDTO;
import chattingapp.dtos.user.register.RegisterVerifyResponseDTO;
import chattingapp.utils.SessionManager; 
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class UserService extends BaseService {

    // ==============================================
    // 1. NHÓM ĐĂNG KÝ VÀ ĐĂNG NHẬP (KHÔNG CẦN TOKEN)
    // ==============================================

    public CompletableFuture<RegisterResponseDTO> register(RegisterRequestDTO dto) {
        HttpRequest request = createPostRequest("/users/register", dto);
        return ApiClient.getClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> handleResponse(response, RegisterResponseDTO.class));
    }

    public CompletableFuture<Void> getRegisterOTP(RegisterOTPRequestDTO dto) {
        HttpRequest request = createPostRequest("/users/get-register-otp", dto);
        return ApiClient.getClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> handleResponse(response, Void.class));
    }

    public CompletableFuture<RegisterVerifyResponseDTO> verifyRegister(RegisterVerifyRequestDTO dto) {
        HttpRequest request = createPostRequest("/users/verify", dto);
        return ApiClient.getClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> handleResponse(response, RegisterVerifyResponseDTO.class));
    }

    public CompletableFuture<LoginResponseDTO> login(LoginRequetDTO dto) {
        HttpRequest request = createPostRequest("/users/login", dto);
        return ApiClient.getClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> handleResponse(response, LoginResponseDTO.class));
    }

    // ==============================================
    // 2. NHÓM TÌM KIẾM VÀ CẬP NHẬT THÔNG TIN CƠ BẢN
    // ==============================================

    public CompletableFuture<SearchDTO> searchUser(String email) {
        String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);
        String token = SessionManager.getToken(); // Lấy Token

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiClient.getBaseUrl() + "/users/search?email=" + encodedEmail))
                .GET()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token) // Gắn token vào
                .build();

        return ApiClient.getClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> handleResponse(response, SearchDTO.class));
    }

    public CompletableFuture<Void> changeProfile(ChangeProfileRequestDTO dto) {
        String token = SessionManager.getToken(); 
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiClient.getBaseUrl() + "/users/change-profile"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(dto)))
                .build();

        return ApiClient.getClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> handleResponse(response, Void.class));
    }

    // ==============================================
    // 3. NHÓM ĐỔI MẬT KHẨU / QUÊN MẬT KHẨU
    // ==============================================

    public CompletableFuture<Void> changePassword(ChangePasswordRequestDTO dto) {
        String token = SessionManager.getToken();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiClient.getBaseUrl() + "/users/change-password"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(dto)))
                .build();

        return ApiClient.getClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> handleResponse(response, Void.class));
    }

    public CompletableFuture<Void> forgotPasswordOTP(ForgotPasswordOTPRequestDTO dto) {
        // Quên mật khẩu thì chưa có token nên dùng createPostRequest bình thường
        HttpRequest request = createPostRequest("/users/forgot-password", dto);
        return ApiClient.getClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> handleResponse(response, Void.class));
    }

    public CompletableFuture<Void> forgotPasswordVerify(ForgotPasswordVerifyRequest dto) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiClient.getBaseUrl() + "/users/forgot-password"))
                .header("Content-Type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(GSON.toJson(dto))) 
                .build();

        return ApiClient.getClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> handleResponse(response, Void.class));
    }

    // ==============================================
    // 4. NHÓM ĐỔI EMAIL (CẦN OTP)
    // ==============================================

    public CompletableFuture<Void> getChangeEmailOTP(ChangeEmailOTPRequestDTO dto) {
        String token = SessionManager.getToken();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiClient.getBaseUrl() + "/users/change-email"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(dto)))
                .build();

        return ApiClient.getClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> handleResponse(response, Void.class));
    }

    public CompletableFuture<Void> verifyChangeEmail(ChangeEmailRequestDTO dto) {
        String token = SessionManager.getToken();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiClient.getBaseUrl() + "/users/change-email"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .method("PATCH", HttpRequest.BodyPublishers.ofString(GSON.toJson(dto)))
                .build();

        return ApiClient.getClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> handleResponse(response, Void.class));
    }
}