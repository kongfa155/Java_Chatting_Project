package chattingapp.services;

import chattingapp.config.ServerConfig;
import java.net.http.HttpClient;
import java.time.Duration;

public class ApiClient {

    private static HttpClient client;
    private static final String BASE_URL = "http:"+ServerConfig.SERVER_URL + "/api";
    private static final String FILE_BASE = "http:"+ServerConfig.SERVER_URL;

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
    public static String getFileUrl(String path) {
    return FILE_BASE + path;
    }
}
