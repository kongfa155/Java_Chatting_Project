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

    //Gửi lời mời kết bạn
    public CompletableFuture<Void> sendFriendRequest(String email) {
        //Tạo HTTP
        String url = ApiClient.getBaseUrl()
                + "/friendships/send"
                + "?email=" + URLEncoder.encode(email, StandardCharsets.UTF_8);
        //Tạo request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.noBody()) //Dữ liệu nằm trong query param, không cần truyền trong body
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getToken()) //Gửi JWT
                .build();
        //Gửi request
        return ApiClient.getClient() //Khởi tạo một httpClient để thực hiện gọi tới server
                .sendAsync(request, HttpResponse.BodyHandlers.ofString()) //Đọc dữ liệu trả về dưới dạng văn bản
                .thenApply(response -> handleResponse(response, Void.class)); //Hàm dùng để parse dữ liệu về kiểu phù hợp
    }
    //Chấp nhận kết bạn
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
    //Từ chối kết bạn
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
    //Lấy danh sách yêu cầu
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

    //Lấy danh sách bạn bè
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
    //Xóa bạn
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
    //Kiểm tra trạng thái bạn bè
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
