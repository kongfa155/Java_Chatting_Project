/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chattingapp.services;

import chattingapp.dtos.FriendLoadDTO;
import chattingapp.dtos.FriendRequestResponseDTO;
import chattingapp.utils.SessionManager;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 *
 * @author CP
 */
public class FriendService extends BaseService {

    // ======================
    // SEND FRIEND REQUEST
    // ======================
    public CompletableFuture<Void> sendFriendRequest(String email) {

        String url = ApiClient.getBaseUrl()
                + "/friendships/send"
                + "?email=" + URLEncoder.encode(email, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.noBody())
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getToken())
                .build();

        return ApiClient.getClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> handleResponse(response, Void.class));
    }

    // ======================
    // ACCEPT FRIEND REQUEST
    // ======================
    public CompletableFuture<Void> acceptFriendRequest(String requestId) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiClient.getBaseUrl() + "/friendships/accept/" + requestId))
                .POST(HttpRequest.BodyPublishers.noBody())
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getToken())
                .build();

        return ApiClient.getClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> handleResponse(response, Void.class));
    }

    // ======================
    // REJECT FRIEND REQUEST
    // ======================
    public CompletableFuture<Void> rejectFriendRequest(String requestId) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiClient.getBaseUrl() + "/friendships/reject/" + requestId))
                .POST(HttpRequest.BodyPublishers.noBody())
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getToken())
                .build();

        return ApiClient.getClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> handleResponse(response, Void.class));
    }

    public CompletableFuture<List<FriendRequestResponseDTO>> getFriendRequests() {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiClient.getBaseUrl() + "/friendships/requests"))
                .GET()
                .header("Authorization", "Bearer " + SessionManager.getToken())
                .build();

        return ApiClient.getClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> handleResponseList(response, FriendRequestResponseDTO.class));
    }

    // ======================
    public CompletableFuture<List<FriendLoadDTO>> getFriends() {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiClient.getBaseUrl() + "/friendships/friends"))
                .GET()
                .header("Authorization", "Bearer " + SessionManager.getToken())
                .build();

        return ApiClient.getClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> handleResponseList(response, FriendLoadDTO.class));
    }

    public CompletableFuture<Void> deleteFriend(String friendId) {

        String url = ApiClient.getBaseUrl()
                + "/friendships/delete"
                + "?friendId=" + URLEncoder.encode(friendId, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.noBody())
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getToken())
                .build();

        return ApiClient.getClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> handleResponse(response, Void.class));
    }

    public CompletableFuture<Boolean> isFriend(String friendId) {
        // Lấy danh sách bạn bè hiện tại và check xem friendId có tồn tại không
        return getFriends().thenApply(friends -> {
            for (FriendLoadDTO f : friends) {
                if (f.getUserId().equals(friendId)) {
                    return true;
                }
            }
            return false;
        });
    }
}
