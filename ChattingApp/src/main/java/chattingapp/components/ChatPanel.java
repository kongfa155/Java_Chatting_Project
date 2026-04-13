/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package chattingapp.components;

import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import chattingapp.models.ChatData;
import chattingapp.models.Message;
import chattingapp.models.MessageType;
import chattingapp.models.NotificationType;
import chattingapp.services.ApiClient;
import chattingapp.services.FriendService;
import chattingapp.services.MessageService;
import chattingapp.services.StompClientService;
import chattingapp.utils.NotificationManager;
import chattingapp.utils.SessionManager;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Desktop;
import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author CP
 */
public class ChatPanel extends javax.swing.JPanel {

    //Lưu user hiện tại
    private String currentChatUserId;
    //Quản lí hiển thị panel lấy file ở bên phải
    private FileDrawerPanel fileDrawer;
    private boolean drawerOpen = false;
    //Thiết lập web socket
    private StompClientService stompClient;
    //Dùng để scroll khi click vào item trong fileDrawer
    private java.util.Map<String, java.awt.Component> messageMap = new java.util.HashMap<>();

    /**
     * Creates new form ChatPanel
     */
    public ChatPanel() {
        //Setup UI cơ bản
        initComponents();
        JScrollPane.getVerticalScrollBar().setUnitIncrement(30);
        ScrollMes.putClientProperty("JTextField.placeholderText", "Type a message...");
        fileDrawer = new FileDrawerPanel();
        fileDrawer.setVisible(false);
        //Thêm panel vào bên phải
        chatContentPanel.add(fileDrawer, BorderLayout.EAST);
        //Gắn event lắng nghe để scroll
        fileDrawer.setFileClickListener(messageId -> {
            java.awt.Component comp = messageMap.get(messageId);
            if (comp != null) {
                ((javax.swing.JComponent) comp).scrollRectToVisible(comp.getBounds());
            }
        });
        fileDrawer.setDeleteFriendListener(() -> {
            deleteFriend();
        });
        showEmpty();
        //Lắng nghe nút enter, nếu có = gửi tin nhắn
        txtMessage.getInputMap().put(
                javax.swing.KeyStroke.getKeyStroke("ENTER"),
                "sendMessage"
        );

        txtMessage.getActionMap().put("sendMessage", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (!isSending) {
                    btnSendActionPerformed(null);
                }
            }
        });

        // Alt + Enter = xuống dòng
        txtMessage.getInputMap().put(
                javax.swing.KeyStroke.getKeyStroke("alt ENTER"),
                "insert-break"
        );

    }

    //Websocket
    private void addSingleMessage(Message msg) {
        System.out.println("Cập nhật giao diện thêm tin nhắn: " + msg.getContent());
        //Điều kiện để lựa bên hiển thị tin nhắn
        boolean isMine
                = msg.getSenderId().equals(
                        SessionManager.getCurrentUser().getUserId()
                );

        switch (msg.getMessageType()) {

            case TEXT -> {
                MessageBubble bubble
                        = new MessageBubble(msg.getContent(), isMine);
                messageContainer.add(bubble);
            }

            default -> {
                // file/image → tạm reload
                loadChatFromCurrent();
                return;
            }
        }

        messageContainer.revalidate();
        messageContainer.repaint();

        javax.swing.SwingUtilities.invokeLater(() -> {
            JScrollPane.revalidate();

            JScrollPane.getVerticalScrollBar().setValue(
                    JScrollPane.getVerticalScrollBar().getMaximum()
            );
        });
    }

    public void resetChat() {
        currentChatUserId = null;

        messageContainer.removeAll();
        messageContainer.revalidate();
        messageContainer.repaint();

        showEmpty();
    }

    private void deleteFriend() {
        FriendService service = new FriendService();

        service.deleteFriend(currentChatUserId)
                .thenRun(() -> {
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        javax.swing.JOptionPane.showMessageDialog(this, "Đã xóa bạn");

                        // reset UI
                        showEmpty();
                        messageContainer.removeAll();
                        messageContainer.repaint();
                    });
                    chattingapp.ui.MainFrame.updateChatList();
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();

                    javax.swing.SwingUtilities.invokeLater(() -> {
                        javax.swing.JOptionPane.showMessageDialog(this, "Xóa thất bại");
                    });

                    return null;
                });
    }

    private File chooseFile() {

        JFileChooser chooser = new JFileChooser();

        // chỉ cho chọn file hợp lệ
        javax.swing.filechooser.FileNameExtensionFilter filter
                = new javax.swing.filechooser.FileNameExtensionFilter(
                        "Supported Files",
                        "png", "jpg", "jpeg",
                        "pdf", "docx", "xlsx", "pptx",
                        "mp4"
                );

        chooser.setFileFilter(filter);

        // chặn luôn "All Files"
        chooser.setAcceptAllFileFilterUsed(false);
        int result = chooser.showOpenDialog(this);
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Images", "png", "jpg", "jpeg"));
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Documents", "pdf", "docx", "xlsx", "pptx"));
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Videos", "mp4"));

        if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }

        return null;
    }

    private void showEmpty() {
        CardLayout cl = (CardLayout) getLayout();
        cl.show(this, "card2");
    }

    private void showChat() {
        CardLayout cl = (CardLayout) getLayout();
        cl.show(this, "card3");
    }

//    private MessageType detectFileType(java.io.File file) {
//        //Kiểm tra kiểu của file => Trả về kiểu loại như mong muốn
//        String name = file.getName().toLowerCase();
//
//        if (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".gif")) {
//            return chattingapp.models.MessageType.IMAGE;
//        }
//
//        if (name.endsWith(".mp4") || name.endsWith(".mkv") || name.endsWith(".mov") || name.endsWith(".avi")) {
//            return chattingapp.models.MessageType.VIDEO;
//        }
//
//        if (name.endsWith(".mp3") || name.endsWith(".wav")) {
//            return chattingapp.models.MessageType.AUDIO;
//        }
//
//        return chattingapp.models.MessageType.FILE;
//    }
    private boolean isWsConnected = false;

    public void initWebSocket() {
        //Log
        System.out.println("Gọi WebSocket");
        //Kiểm tra xem đã có WS chưa, nếu có thì không tạo lại
        if (isWsConnected) {
            System.out.println("WS đã có");
            return;
        }
        //Kiểm tra xem đã login chưa, nếu chưa delay xong lặp lại sau 500ms
        if (SessionManager.getUserId() == null) {
            new javax.swing.Timer(500, e -> {
                if (SessionManager.getUserId() != null) {
                    ((javax.swing.Timer) e.getSource()).stop();
                    initWebSocket();
                }
            }).start();
            return;
        }
        //Tạo kết nối Websocket
        if (stompClient == null) {
            stompClient = new StompClientService();
        }

        // Gọi hàm connect mới với 2 listener
        stompClient.connect(
                message -> {
                    handleIncomingMessage(message);
                },
                notification -> {

                    System.out.println("🔥 NHẬN NOTI: " + notification.getContent());

                    // 🚫 Nếu đang chat với người gửi → bỏ qua
                    if (notification.getType() == NotificationType.MESSAGE) {

                        // ⚠️ notification.getUserId() = receiver (mày)
                        // nên phải lấy sender từ content hoặc thêm field senderId
                        // 👉 TEMP FIX (dựa vào currentChatUserId)
                        if (currentChatUserId != null
                        && notification.getContent().contains(currentChatUserId)) {

                            System.out.println("🚫 Đang chat → bỏ notification");
                            return;
                        }
                    }

                    NotificationManager.add(notification);

                    javax.swing.SwingUtilities.invokeLater(() -> {
                        chattingapp.components.SideBarPanel.updateNotificationUI();
                        chattingapp.components.SideBarPanel.updateBadgeExternal();
                    });
                }
        );

        isWsConnected = true;
    }

    public void handleIncomingMessage(Message message) {

        System.out.println("WebSocket đã nhận được tin nhắn: " + message.getContent());
        chattingapp.ui.MainFrame.updateChatList();

        String myId = SessionManager.getCurrentUser().getUserId();

        boolean isCurrentChat = currentChatUserId != null
                && ((message.getSenderId().equals(currentChatUserId) && message.getReceiverId().equals(myId))
                || (message.getSenderId().equals(myId) && message.getReceiverId().equals(currentChatUserId)));

        if (isCurrentChat) {
            javax.swing.SwingUtilities.invokeLater(() -> {
                addSingleMessage(message);
            });
        }
    }

    private void renderMessages(java.util.List<Message> messages) {
        messageContainer.removeAll();
        //Lấy toàn bộ tin nhắn
        for (Message msg : messages) {

            boolean isMine
                    = msg.getSenderId().equals(
                            SessionManager.getCurrentUser().getUserId()
                    );

            switch (msg.getMessageType()) {
                //Nếu là hình ảnh
                case IMAGE -> {
                    try {
                        String imageUrl = ApiClient.getFileUrl(msg.getFileUrl());

                        URL url = new URL(imageUrl);
                        //Load từ URL
                        java.awt.image.BufferedImage bufferedImage
                                = javax.imageio.ImageIO.read(url);

                        if (bufferedImage == null) {
                            throw new RuntimeException("Image null");
                        }
                        //Scale ảnh lại
                        Image img = bufferedImage.getScaledInstance(220, -1, Image.SCALE_SMOOTH);

                        JLabel imageLabel = new JLabel(new ImageIcon(img));

                        // 🎯 PANEL CHA
                        JPanel imagePanel = new JPanel(new BorderLayout());
                        imagePanel.setOpaque(false);

                        imagePanel.add(imageLabel, BorderLayout.CENTER);

                        // Tạo nút download
                        JButton downloadBtn = new JButton("⬇");
                        downloadBtn.setFocusPainted(false);

                        downloadBtn.addActionListener(e -> {
                            try {
                                String downloadUrl = ApiClient.getFileUrl(msg.getFileUrl());

                                String fileName = msg.getContent() != null
                                        ? msg.getContent()
                                        : "image.jpg";

                                String downloadPath = System.getProperty("user.home") + "/Downloads/";
                                File saveFile = new File(downloadPath + fileName);

                                try (java.io.InputStream in = new URL(downloadUrl).openStream()) {
                                    java.nio.file.Files.copy(
                                            in,
                                            saveFile.toPath(),
                                            java.nio.file.StandardCopyOption.REPLACE_EXISTING
                                    );
                                }

                                // mở folder
                                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                                    Runtime.getRuntime().exec(new String[]{
                                        "explorer.exe",
                                        "/select,",
                                        saveFile.getAbsolutePath()
                                    });
                                } else {
                                    Desktop.getDesktop().open(saveFile.getParentFile());
                                }

                                System.out.println("Downloaded: " + saveFile.getAbsolutePath());

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        });

                        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                        topRight.setOpaque(false);
                        topRight.add(downloadBtn);

                        imagePanel.add(topRight, BorderLayout.NORTH);

                        // 🎯 WRAPPER (trái/phải)
                        JPanel wrapper = new JPanel(new FlowLayout(
                                isMine ? FlowLayout.RIGHT : FlowLayout.LEFT
                        ));
                        wrapper.setOpaque(false);

                        wrapper.add(imagePanel);
                        messageContainer.add(wrapper);
                        messageMap.put(msg.getMessageId(), wrapper);

                    } catch (Exception e) {
                        System.out.println("❌ " + e.getMessage());

                        JLabel error = new JLabel("⚠ Không load được ảnh");
                        messageContainer.add(error);
                    }
                }

                case FILE, VIDEO, AUDIO -> {
                    String fileName = msg.getContent() != null
                            ? msg.getContent()
                            : "file";

                    JPanel filePanel = new JPanel();
                    filePanel.setLayout(new BorderLayout());
                    filePanel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                    filePanel.setBackground(isMine ? new Color(0, 120, 255) : new Color(240, 240, 240));

                    // 🧠 LEFT: icon + name
                    JPanel left = new JPanel(new BorderLayout());
                    left.setOpaque(false);

                    JLabel icon = new JLabel("📄");
                    icon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));

                    JLabel name = new JLabel(fileName);
                    name.setForeground(isMine ? Color.WHITE : Color.BLACK);

                    left.add(icon, BorderLayout.WEST);
                    left.add(name, BorderLayout.CENTER);

                    // 🧠 RIGHT: download button
                    JButton downloadBtn = new JButton("⬇");
                    downloadBtn.setFocusPainted(false);

                    downloadBtn.addActionListener(e -> {
                        try {
                            String fileUrl = "http://localhost:8080" + msg.getFileUrl();

                            // 🧠 tên file
                            String downloadName = msg.getContent() != null
                                    ? msg.getContent()
                                    : "file";

                            // 🧠 folder Downloads
                            String downloadPath = System.getProperty("user.home") + "/Downloads/";

                            // 🧠 file đích
                            File saveFile = new File(downloadPath + downloadName);

                            // 🧠 tải file
                            try (java.io.InputStream in = new URL(fileUrl).openStream()) {
                                java.nio.file.Files.copy(
                                        in,
                                        saveFile.toPath(),
                                        java.nio.file.StandardCopyOption.REPLACE_EXISTING
                                );
                            }

                            // 🧠 mở folder
//                            String path = saveFile.getAbsolutePath();
                            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                                Runtime.getRuntime().exec(new String[]{
                                    "explorer.exe",
                                    "/select,",
                                    saveFile.getAbsolutePath()
                                });
                            } else {
                                Desktop.getDesktop().open(saveFile.getParentFile());
                            }
                            System.out.println("Downloaded: " + saveFile.getAbsolutePath());

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                    JPanel right = new JPanel(new BorderLayout());
                    right.setOpaque(false);
                    right.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));

                    right.add(downloadBtn, BorderLayout.CENTER);

                    filePanel.add(left, BorderLayout.CENTER);
                    filePanel.add(right, BorderLayout.EAST);

                    // wrapper
                    JPanel wrapper = new JPanel(new FlowLayout(
                            isMine ? FlowLayout.RIGHT : FlowLayout.LEFT
                    ));
                    wrapper.setOpaque(false);

                    wrapper.add(filePanel);
                    messageContainer.add(wrapper);
                    messageMap.put(msg.getMessageId(), wrapper);
                }

                case TEXT -> {
                    MessageBubble bubble
                            = new MessageBubble(msg.getContent(), isMine);
                    messageContainer.add(bubble);
                }
            }
        }

        messageContainer.revalidate();
        messageContainer.repaint();

        javax.swing.SwingUtilities.invokeLater(() -> {
            javax.swing.JScrollBar vertical = JScrollPane.getVerticalScrollBar();

            vertical.setValue(vertical.getMaximum());
        }
        );
    }

    public void loadChat(ChatData data) {
        //Hiển thị ui chat
        showChat();
        if (data == null) {
            return;
        }
        drawerOpen = false;
        fileDrawer.setVisible(false);

        currentChatUserId = data.getContact().getUserId();
        //Lấy tên người đang nhắn tin
        lblName.setText(data.getContact().getDisplayName());
        lblStatus.setText("Online");

        MessageService service = new MessageService();

        service.getConversation(currentChatUserId)
                .thenAccept(messages -> {
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        //Load toàn bộ chat cũ
                        renderMessages(messages);
                    });
                });
    }

    private void loadChatFromCurrent() {
        //Reload chat hiện tại
        MessageService service = new MessageService();

        service.getConversation(currentChatUserId)
                .thenAccept(messages -> {
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        renderMessages(messages);
                    });
                });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        emptyStatePanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        chatContentPanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        textWrapper = new javax.swing.JPanel();
        lblName = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        optionPanel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        bottomWrapper = new javax.swing.JPanel();
        inputPanel = new javax.swing.JPanel();
        btnAttach = new javax.swing.JButton();
        ScrollMes = new javax.swing.JScrollPane();
        txtMessage = new javax.swing.JTextArea();
        btnSend = new javax.swing.JButton();
        JScrollPane = new javax.swing.JScrollPane();
        messageContainer = new javax.swing.JPanel();

        setName("ChatPanel"); // NOI18N
        setLayout(new java.awt.CardLayout());

        emptyStatePanel.setBackground(new java.awt.Color(204, 255, 255));
        emptyStatePanel.setName("emptyStatePanel"); // NOI18N
        emptyStatePanel.setLayout(new java.awt.BorderLayout());

        jLabel4.setBackground(new java.awt.Color(153, 255, 255));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Chọn một đoạn chat để bắt đầu !!!");
        emptyStatePanel.add(jLabel4, java.awt.BorderLayout.CENTER);

        add(emptyStatePanel, "card2");

        chatContentPanel.setLayout(new java.awt.BorderLayout());

        headerPanel.setBackground(new java.awt.Color(255, 255, 255));
        headerPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(230, 230, 230)));
        headerPanel.setPreferredSize(new java.awt.Dimension(0, 60));
        headerPanel.setLayout(new java.awt.BorderLayout());

        textWrapper.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 15));
        textWrapper.setOpaque(false);
        textWrapper.setLayout(new javax.swing.BoxLayout(textWrapper, javax.swing.BoxLayout.Y_AXIS));

        lblName.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblName.setText("FriendName");
        textWrapper.add(lblName);

        lblStatus.setBackground(new java.awt.Color(255, 255, 255));
        lblStatus.setForeground(new java.awt.Color(0, 150, 0));
        lblStatus.setText("Online");
        textWrapper.add(lblStatus);

        headerPanel.add(textWrapper, java.awt.BorderLayout.WEST);

        optionPanel.setLayout(new java.awt.BorderLayout());

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton1.setText("...");
        jButton1.setFocusable(false);
        jButton1.setPreferredSize(new java.awt.Dimension(45, 40));
        jButton1.addActionListener(this::jButton1ActionPerformed);
        optionPanel.add(jButton1, java.awt.BorderLayout.CENTER);

        headerPanel.add(optionPanel, java.awt.BorderLayout.EAST);

        chatContentPanel.add(headerPanel, java.awt.BorderLayout.NORTH);

        bottomWrapper.setBackground(new java.awt.Color(255, 255, 255));
        bottomWrapper.setLayout(new java.awt.BorderLayout());

        inputPanel.setBackground(new java.awt.Color(255, 255, 255));
        inputPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.setPreferredSize(new java.awt.Dimension(0, 60));
        inputPanel.setLayout(new java.awt.BorderLayout());

        btnAttach.setText("+");
        btnAttach.setPreferredSize(new java.awt.Dimension(45, 40));
        btnAttach.addActionListener(this::btnAttachActionPerformed);
        inputPanel.add(btnAttach, java.awt.BorderLayout.LINE_START);

        txtMessage.setColumns(20);
        txtMessage.setRows(5);
        ScrollMes.setViewportView(txtMessage);

        inputPanel.add(ScrollMes, java.awt.BorderLayout.CENTER);

        btnSend.setText("Send");
        btnSend.setPreferredSize(new java.awt.Dimension(70, 40));
        btnSend.addActionListener(this::btnSendActionPerformed);
        inputPanel.add(btnSend, java.awt.BorderLayout.EAST);

        bottomWrapper.add(inputPanel, java.awt.BorderLayout.CENTER);

        chatContentPanel.add(bottomWrapper, java.awt.BorderLayout.SOUTH);

        JScrollPane.setBackground(new java.awt.Color(255, 255, 255));
        JScrollPane.setBorder(null);

        messageContainer.setBackground(new java.awt.Color(255, 255, 255));
        messageContainer.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 15));
        messageContainer.setLayout(new javax.swing.BoxLayout(messageContainer, javax.swing.BoxLayout.Y_AXIS));
        JScrollPane.setViewportView(messageContainer);

        chatContentPanel.add(JScrollPane, java.awt.BorderLayout.CENTER);

        add(chatContentPanel, "card3");
    }// </editor-fold>//GEN-END:initComponents

    private void btnAttachActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAttachActionPerformed
        File file = chooseFile();
        if (file == null) {
            return;
        }
        //Chọn file
        //Gửi lên BE
        MessageService service = new MessageService();

        service.sendFile(currentChatUserId, file)
                .thenAccept(msg -> {
                    javax.swing.SwingUtilities.invokeLater(() -> {

                    });
                });
    }//GEN-LAST:event_btnAttachActionPerformed
    //Đóng mở drawer bên phải
    private void toggleDrawer() {

        drawerOpen = !drawerOpen;

        fileDrawer.setVisible(drawerOpen);

        if (drawerOpen) {
            //Nữa MiMi viết cho tui hàm lấy toàn bộ chat mà không phải text của mình với bạn hiện tại tại chỗ này nhe

            MessageService service = new MessageService();

            service.getConversation(currentChatUserId)
                    .thenAccept(messages -> {

                        javax.swing.SwingUtilities.invokeLater(() -> {

                            fileDrawer.loadFiles(messages);

                        });

                    });
        }

        revalidate();
        repaint();
    }
    private boolean isSending = false;

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        //Kiểm tra gửi chưa, chặn gửi nhiều lần
        if (isSending) {
            return;
        }
        //Lấy nội dung
        String text = txtMessage.getText().trim();
        if (text.isEmpty()) {
            return;
        }

        isSending = true;

//        String myId = SessionManager.getCurrentUser().getUserId();
        // 🔥 render ngay (optimistic UI)
        txtMessage.setText("");

        MessageService service = new MessageService();
        //Gọi API gửi tin nhắn
        service.sendMessage(currentChatUserId, text)
                .thenAccept(msg -> {

                    javax.swing.SwingUtilities.invokeLater(() -> {
                        isSending = false;
                    });

                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    isSending = false;
                    return null;
                });
    }//GEN-LAST:event_btnSendActionPerformed
    public StompClientService getStompClientService() {
        return stompClient;
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        toggleDrawer();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane JScrollPane;
    private javax.swing.JScrollPane ScrollMes;
    private javax.swing.JPanel bottomWrapper;
    private javax.swing.JButton btnAttach;
    private javax.swing.JButton btnSend;
    private javax.swing.JPanel chatContentPanel;
    private javax.swing.JPanel emptyStatePanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel inputPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JPanel messageContainer;
    private javax.swing.JPanel optionPanel;
    private javax.swing.JPanel textWrapper;
    private javax.swing.JTextArea txtMessage;
    // End of variables declaration//GEN-END:variables
}
