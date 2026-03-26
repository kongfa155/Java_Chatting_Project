package chattingapp.services;

import chattingapp.models.Message;
import chattingapp.utils.SessionManager;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.Arrays;

public class StompClientService {

    private WebSocketStompClient stompClient;
    private StompSession currentSession;

    public interface MessageListener {
        void onMessage(Message message);
    }

    // Giữ nguyên tên hàm cũ
    public void connect(MessageListener listener) {
        String userId = SessionManager.getUserId();
        if (userId == null) return;

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
                    currentSession = session;

                    // Vẫn giữ subscribe mặc định theo UserID để nhận thông báo hệ thống/cuộc gọi nếu cần
                    session.subscribe("/topic/messages/" + userId, new StompFrameHandler() {
                        @Override
                        public Type getPayloadType(StompHeaders headers) { return Message.class; }
                        @Override
                        public void handleFrame(StompHeaders headers, Object payload) {
                            listener.onMessage((Message) payload);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tên hàm mới: subscribeToConversation
     * Mục đích: Tạo ra chatId duy nhất cho 2 người và lắng nghe chung 1 kênh
     */
    public void subscribeToConversation(String myId, String partnerId, MessageListener listener) {
        if (currentSession == null || !currentSession.isConnected()) return;

        // Tạo chatId duy nhất bằng cách sắp xếp 2 ID (Ví dụ: "user1_user2")
        String[] ids = {myId, partnerId};
        Arrays.sort(ids);
        String chatId = ids[0] + "_" + ids[1];

        System.out.println("📡 SUBSCRIBING TO ROOM: /topic/chatroom/" + chatId);

        currentSession.subscribe("/topic/chatroom/" + chatId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Message.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Message msg = (Message) payload;
                listener.onMessage(msg);
            }
        });
    }
}