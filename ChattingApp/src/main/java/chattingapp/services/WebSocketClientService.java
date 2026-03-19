
package chattingapp.services;

import chattingapp.models.Message;
import chattingapp.utils.SessionManager;
import com.google.gson.Gson;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class WebSocketClientService {

    private WebSocketClient client;
    private MessageListener listener;
    private Gson gson = new Gson();

    public interface MessageListener {
        void onMessageReceived(Message message);
    }

    public void connect(MessageListener listener) {
        this.listener = listener;

        try {
            client = new WebSocketClient(new URI("ws://localhost:8080/ws/websocket")) {

                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    System.out.println("✅ WebSocket connected");
                }

                @Override
                public void onMessage(String message) {
                    try {
                        Message msg = gson.fromJson(message, Message.class);

                        if (listener != null) {
                            listener.onMessageReceived(msg);
                        }

                    } catch (Exception e) {
                        System.out.println("❌ parse error: " + e.getMessage());
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("❌ WebSocket closed");
                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                }
            };

            client.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}