package chattingapp.services;

import chattingapp.config.ServerConfig;
import chattingapp.models.Message;
import chattingapp.models.Notification;
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

    // Interface để lắng nghe tin nhắn
    public interface MessageListener {

        void onMessage(Message message);
    }

    // Interface để lắng nghe thông báo
    public interface NotificationListener {

        void onNotification(Notification notification);
    }

    // Sửa hàm connect để nhận cả 2 listener ngay từ đầu
    public void connect(MessageListener msgListener, NotificationListener notiListener) {
        String userId = SessionManager.getUserId();
        if (userId == null) {
            return;
        }

        try {
            stompClient = new WebSocketStompClient(new StandardWebSocketClient());
            MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
            converter.setObjectMapper(mapper);
            stompClient.setMessageConverter(converter);

            stompClient.connectAsync("ws:" + ServerConfig.SERVER_URL + "/ws", new StompSessionHandlerAdapter() {
                @Override
                public void afterConnected(StompSession session, StompHeaders headers) {
                    System.out.println("✅ WS CONNECTED SUCCESSFULLY");
                    currentSession = session;

                    // 1. Subscribe nhận tin nhắn cá nhân (Hệ thống/Backup)
                    subscribeToTopic("/topic/messages/" + userId, Message.class, payload -> {
                        msgListener.onMessage((Message) payload);
                    });

                    // 2. Subscribe nhận THÔNG BÁO (Chuông báo) - ĐĂNG KÝ NGAY TẠI ĐÂY
                    subscribeToTopic("/topic/notifications/" + userId, Notification.class, payload -> {
                        System.out.println("🔔 NOTIFICATION RECEIVED VIA WS: " + ((Notification) payload).getContent());
                        notiListener.onNotification((Notification) payload);
                    });
                }

                @Override
                public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
                    System.err.println("❌ WS ERROR: " + exception.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Hàm tiện ích để subscribe gọn hơn
    private void subscribeToTopic(String topic, Class<?> clazz, java.util.function.Consumer<Object> handler) {
        currentSession.subscribe(topic, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return clazz;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                handler.accept(payload);
            }
        });
        System.out.println("📡 Subscribed to: " + topic);
    }

    public void subscribeToConversation(String myId, String partnerId, MessageListener listener) {
        if (currentSession == null || !currentSession.isConnected()) {
            return;
        }

        String[] ids = {myId, partnerId};
        Arrays.sort(ids);
        String chatId = ids[0] + "_" + ids[1];

        subscribeToTopic("/topic/chatroom/" + chatId, Message.class, payload -> {
            listener.onMessage((Message) payload);
        });
    }

    // Giữ lại cho tương thích nếu cần gọi lẻ, nhưng nên dùng trong connect
    public void subscribeToNotifications(String userId, NotificationListener listener) {
        if (currentSession == null || !currentSession.isConnected()) {
            return;
        }
        subscribeToTopic("/topic/notifications/" + userId, Notification.class, payload -> {
            listener.onNotification((Notification) payload);
        });
    }

    public boolean isConnected() {
        return currentSession != null && currentSession.isConnected();
    }
}
