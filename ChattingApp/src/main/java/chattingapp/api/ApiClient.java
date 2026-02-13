package chattingapp.api;

import java.net.http.HttpClient;
import java.time.Duration;

public class ApiClient {

    private static HttpClient client;
    private static final String BASE_URL = "http://localhost:8080/api";

    public static HttpClient getClient(){
        if(client==null){
            client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();
        }
        return client;
    }
    public static String getBaseUrl(){
        return BASE_URL;
    }
}
