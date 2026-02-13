package chattingapp.api;
import chattingapp.responses.ApiResponse;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class BaseService {
    protected final ObjectMapper objectMapper = new ObjectMapper();
    protected <T> T handleResponse(HttpResponse<String> response, Class<T> dataType) {
        try {
            JavaType type = objectMapper.getTypeFactory()
                    .constructParametricType(ApiResponse.class, dataType);

            ApiResponse<T> apiResponse = objectMapper.readValue(response.body(), type);

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return apiResponse.getData();
            } else {
                throw new RuntimeException(apiResponse.getErrorCode() + ": " + apiResponse.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException("System Error: " + e.getMessage());
        }
    }
    protected HttpRequest createGetRequest(String endpoint){
        return HttpRequest.newBuilder()
                .uri(URI.create(ApiClient.getBaseUrl()+endpoint))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .GET()
                .build();
    }
    protected HttpRequest createPostRequest(String endpoint, Object dto){
        try{
            String jsonBody = objectMapper.writeValueAsString(dto);
            return HttpRequest.newBuilder()
                    .uri(URI.create(ApiClient.getBaseUrl()+endpoint))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
        } catch (Exception e){
            throw new RuntimeException("Error when converting DTO to JSON in Service layer", e);
        }


    }

    protected HttpRequest createPutRequest(String endpoint, Object dto){
        try{
            String jsonBody = objectMapper.writeValueAsString(dto);
            return HttpRequest.newBuilder()
                    .uri(URI.create(ApiClient.getBaseUrl()+endpoint))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
        } catch (Exception e){
            throw new RuntimeException("Error when converting DTO to JSON in Service layer", e);
        }
    }
    protected HttpRequest createPatchRequest(String endpoint, Object dto) {
        try{
            String jsonBody = objectMapper.writeValueAsString(dto);
            return HttpRequest.newBuilder()
                    .uri(URI.create(ApiClient.getBaseUrl()+endpoint))
                    .header("Content-Type", "application/json")
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
        } catch (Exception e){
            throw new RuntimeException("Error when converting DTO to JSON in Service layer", e);
        }
    }

    protected HttpRequest createDeleteRequest(String endpoint) {
        return HttpRequest.newBuilder()
                .uri(URI.create(ApiClient.getBaseUrl() + endpoint))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
    }

}
