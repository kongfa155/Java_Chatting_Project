package chattingapp.services;

import chattingapp.models.Message;
import chattingapp.utils.SessionManager;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MessageService extends BaseService {

    // ======================
    // LOAD CONVERSATION
    // ======================
    public CompletableFuture<List<Message>> getConversation(String userId) {

        String url = ApiClient.getBaseUrl()
                + "/messages/conversation?userId=" + userId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Authorization", "Bearer " + SessionManager.getToken())
                .build();

        return ApiClient.getClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {

                System.out.println("RAW RESPONSE:");
                System.out.println(response.body());

                return handleResponseList(response, Message.class);
                     }   
                );
    }
    public CompletableFuture<Message> sendMessage(String receiverId, String content) {

    String url = ApiClient.getBaseUrl() + "/messages/send";

        String json = """
            {
              "receiverId": "%s",
              "content": "%s"
            }
            """.formatted(receiverId, content);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + SessionManager.getToken())
            .build();

        return ApiClient.getClient()
            .sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(res -> handleResponse(res, Message.class));
    }

}