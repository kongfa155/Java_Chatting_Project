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
import chattingapp.services.ApiClient;
import chattingapp.services.MessageService;
import chattingapp.services.StompClientService;
import chattingapp.utils.SessionManager;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Desktop;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
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

    private String currentChatUserId;
    private FileDrawerPanel fileDrawer;
    private boolean drawerOpen = false;
    private StompClientService stompClient;

    /**
     * Creates new form ChatPanel
     */
    public ChatPanel() {
        initComponents();
        JScrollPane.getVerticalScrollBar().setUnitIncrement(30);
        ScrollMes.putClientProperty("JTextField.placeholderText", "Type a message...");
        fileDrawer = new FileDrawerPanel();
        fileDrawer.setVisible(false);

        chatContentPanel.add(fileDrawer, BorderLayout.EAST);
        showEmpty();
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
        System.out.println("UI ADD MESSAGE: " + msg.getContent());
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

        JScrollPane.getVerticalScrollBar().setValue(
                JScrollPane.getVerticalScrollBar().getMaximum()
        );
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

        // ❗ chặn luôn "All Files"
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

    private MessageType detectFileType(java.io.File file) {

        String name = file.getName().toLowerCase();

        if (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".gif")) {
            return chattingapp.models.MessageType.IMAGE;
        }

        if (name.endsWith(".mp4") || name.endsWith(".mkv") || name.endsWith(".mov") || name.endsWith(".avi")) {
            return chattingapp.models.MessageType.VIDEO;
        }

        if (name.endsWith(".mp3") || name.endsWith(".wav")) {
            return chattingapp.models.MessageType.AUDIO;
        }

        return chattingapp.models.MessageType.FILE;
    }

    public void initWebSocket() {

        if (SessionManager.getUserId() == null) {
            System.out.println("⏳ đợi userId...");

            new javax.swing.Timer(500, e -> {
                if (SessionManager.getUserId() != null) {
                    ((javax.swing.Timer) e.getSource()).stop();
                    initWebSocket();
                }
            }).start();

            return;
        }

        stompClient = new StompClientService();

        stompClient.connect(message -> {

            System.out.println("📩 WS: " + message.getContent());

            if (currentChatUserId == null) {
                return;
            }

            String myId = SessionManager.getCurrentUser().getUserId();

            boolean isCurrentChat
                    = (message.getSenderId().equals(myId)
                    && message.getReceiverId().equals(currentChatUserId))
                    || (message.getSenderId().equals(currentChatUserId)
                    && message.getReceiverId().equals(myId));

            if (!isCurrentChat) {
                return;
            }

            javax.swing.SwingUtilities.invokeLater(() -> {
                addSingleMessage(message);
            });
        });
    }

    private void renderMessages(java.util.List<Message> messages) {
        messageContainer.removeAll();

        for (Message msg : messages) {

            boolean isMine
                    = msg.getSenderId().equals(
                            SessionManager.getCurrentUser().getUserId()
                    );

            switch (msg.getMessageType()) {

                case IMAGE -> {
                    try {
                        String imageUrl = ApiClient.getFileUrl(msg.getFileUrl());

                        URL url = new URL(imageUrl);

                        java.awt.image.BufferedImage bufferedImage
                                = javax.imageio.ImageIO.read(url);

                        if (bufferedImage == null) {
                            throw new RuntimeException("Image null");
                        }

                        Image img = bufferedImage.getScaledInstance(220, -1, Image.SCALE_SMOOTH);

                        JLabel imageLabel = new JLabel(new ImageIcon(img));

                        // 🎯 PANEL CHA
                        JPanel imagePanel = new JPanel(new BorderLayout());
                        imagePanel.setOpaque(false);

                        imagePanel.add(imageLabel, BorderLayout.CENTER);

                        // 🎯 NÚT DOWNLOAD
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
                            String path = saveFile.getAbsolutePath();

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

        JScrollPane.getVerticalScrollBar().setValue(
                JScrollPane.getVerticalScrollBar().getMaximum()
        );
    }

    public void loadChat(ChatData data) {
        showChat();
        if (data == null) {
            return;
        }

        currentChatUserId = data.getContact().getUserId();
        lblName.setText(data.getContact().getDisplayName());
        lblStatus.setText("Online");

        MessageService service = new MessageService();

        service.getConversation(currentChatUserId)
                .thenAccept(messages -> {
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        renderMessages(messages);
                    });
                });
    }

    private void loadChatFromCurrent() {
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

        MessageService service = new MessageService();

        service.sendFile(currentChatUserId, file)
                .thenAccept(msg -> {
                    javax.swing.SwingUtilities.invokeLater(() -> {

                    });
                });
    }//GEN-LAST:event_btnAttachActionPerformed
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
         if (isSending) return;

    String text = txtMessage.getText().trim();
    if (text.isEmpty()) return;

    isSending = true;

    String myId = SessionManager.getCurrentUser().getUserId();

    // 🔥 render ngay (optimistic UI)
    Message fakeMsg = new Message();
    fakeMsg.setContent(text);
    fakeMsg.setSenderId(myId);
    fakeMsg.setReceiverId(currentChatUserId);
    fakeMsg.setMessageType(MessageType.TEXT);

    addSingleMessage(fakeMsg);

    txtMessage.setText("");

    MessageService service = new MessageService();

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
