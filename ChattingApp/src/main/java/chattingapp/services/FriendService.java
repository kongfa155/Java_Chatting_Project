/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chattingapp.services;

import chattingapp.dtos.FriendRequestResponseDTO;
import chattingapp.dtos.SendFriendDTO;
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
}
