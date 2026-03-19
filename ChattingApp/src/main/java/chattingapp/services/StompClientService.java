
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

    String userId = SessionManager.getUserId();

    if (userId == null) {
        System.out.println("❌ WS NOT CONNECT: userId null");
        return;
    }

    System.out.println("🔥 WS START with userId = " + userId);

    try {
       stompClient = new WebSocketStompClient(new StandardWebSocketClient());

        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();

        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

        converter.setObjectMapper(mapper);

        stompClient.setMessageConverter(converter);

        stompClient.connectAsync("ws://localhost:8080/ws", new StompSessionHandlerAdapter() {

            @Override
            public void afterConnected(StompSession session, StompHeaders headers) {

                System.out.println("✅ WS CONNECTED");

                session.subscribe("/topic/messages/" + userId, new StompFrameHandler() {

                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return Message.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {

                        Message msg = (Message) payload;

                        System.out.println("📥 WS RECEIVE: " + msg.getContent());

                        listener.onMessage(msg);
                    }
                });
            }

            @Override
            public void handleTransportError(StompSession session, Throwable exception) {
                System.out.println("❌ WS ERROR:");
                exception.printStackTrace();
            }

            @Override
            public void handleException(StompSession session,
                                        StompCommand command,
                                        StompHeaders headers,
                                        byte[] payload,
                                        Throwable exception) {

                System.out.println("❌ WS EXCEPTION:");
                exception.printStackTrace();
            }
        });

    } catch (Exception e) {
        System.out.println("❌ WS INIT FAIL:");
        e.printStackTrace();
    }
}
}