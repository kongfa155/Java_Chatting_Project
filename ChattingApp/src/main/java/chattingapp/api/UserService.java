package chattingapp.api;

import chattingapp.dtos.RegisterRequestDTO;
import chattingapp.dtos.RegisterResponseDTO;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class UserService extends BaseService{
    public CompletableFuture<RegisterResponseDTO> register(RegisterRequestDTO dto){

        HttpRequest request = createPostRequest("/users/register", dto);
        return ApiClient.getClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> handleResponse(response, RegisterResponseDTO.class));
    }
}
