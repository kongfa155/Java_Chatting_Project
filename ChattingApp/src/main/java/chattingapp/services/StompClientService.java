
package chattingapp.services;

import chattingapp.models.Message;
import chattingapp.utils.SessionManager;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;

public class StompClientService {

    private WebSocketStompClient stompClient;

    public interface MessageListener {
        void onMessage(Message message);
    }

    public void connect(MessageListener listener) {

        stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        stompClient.connect("ws://localhost:8080/ws", new StompSessionHandlerAdapter() {

            @Override
            public void afterConnected(StompSession session, StompHeaders headers) {
                
                
                System.out.println("✅ STOMP connected");

                String userId = SessionManager.getUserId();
                   System.out.println("✅ WS CONNECTED");

        System.out.println("USER ID: " + userId);
if (userId == null || userId.isEmpty()) {
    System.out.println("❌ userId null → không connect websocket");
    return;
}
                session.subscribe("/topic/messages/" + userId, new StompFrameHandler() {

                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return Message.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        listener.onMessage((Message) payload);
                    }
                });
            }
        });
    }
}