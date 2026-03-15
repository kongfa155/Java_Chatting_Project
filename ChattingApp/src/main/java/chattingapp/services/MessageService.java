package chattingapp.services;

import chattingapp.models.Message;
import chattingapp.utils.SessionManager;
import java.io.IOException;

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
    public CompletableFuture<Message> sendFile(String receiverId, java.io.File file) {

    try {

        String boundary = "----ChatAppBoundary" + System.currentTimeMillis();

        java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
        java.io.PrintWriter writer = new java.io.PrintWriter(
                new java.io.OutputStreamWriter(bos, java.nio.charset.StandardCharsets.UTF_8),
                true
        );

        // receiverId
        writer.append("--" + boundary).append("\r\n");
        writer.append("Content-Disposition: form-data; name=\"receiverId\"").append("\r\n");
        writer.append("\r\n");
        writer.append(receiverId).append("\r\n");

        // file header
        writer.append("--" + boundary).append("\r\n");
        writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"").append("\r\n");
        writer.append("Content-Type: application/octet-stream").append("\r\n");
        writer.append("\r\n");
        writer.flush();

        java.nio.file.Files.copy(file.toPath(), bos);

        bos.write("\r\n".getBytes());

        writer.append("--" + boundary + "--").append("\r\n");
        writer.close();

        String url = ApiClient.getBaseUrl() + "/messages/send-file";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofByteArray(bos.toByteArray()))
                .header("Authorization", "Bearer " + SessionManager.getToken())
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .build();

        return ApiClient.getClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(res -> handleResponse(res, Message.class));

    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}

}