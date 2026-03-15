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
import chattingapp.utils.SessionManager;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Image;
import java.io.File;
import java.net.MalformedURLException;
import javax.swing.ImageIcon;
import java.net.URL;

/**
 *
 * @author CP
 */
public class ChatPanel extends javax.swing.JPanel {

    private String currentChatUserId;
    private FileDrawerPanel fileDrawer;
    private boolean drawerOpen = false;

    /**
     * Creates new form ChatPanel
     */
    public ChatPanel() {
        initComponents();
        ScrollMes.putClientProperty("JTextField.placeholderText", "Type a message...");
        fileDrawer = new FileDrawerPanel();
        fileDrawer.setVisible(false);

        chatContentPanel.add(fileDrawer, BorderLayout.EAST);
        showEmpty();
    }

    private File chooseFile() {

        javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();

        int result = chooser.showOpenDialog(this);

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

    private void sendMessage() {

        String text = txtMessage.getText().trim();
        if (text.isEmpty()) {
            return;
        }

        MessageBubble bubble
                = new MessageBubble(text, true);

        messageContainer.add(bubble);
        messageContainer.revalidate();
        messageContainer.repaint();

        txtMessage.setText("");
        MessageBubble friendMsg
                = new MessageBubble("Hello bro", false);

        messageContainer.add(friendMsg);
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

    public void loadChat(ChatData data) {
        showChat();
        if (data == null) {
            return;
        }
        currentChatUserId = data.getContact().getUserId();
        lblName.setText(data.getContact().getDisplayName());
        lblStatus.setText("Online");
        System.out.println("LOAD CHAT CALLED");

        MessageService messageService = new MessageService();

        messageService.getConversation(data.getContact().getUserId())
                .thenAccept(messages -> {

                    javax.swing.SwingUtilities.invokeLater(() -> {

                        messageContainer.removeAll();

                  for (Message msg : messages) {
                           System.out.println(msg.getMessageType());
                            System.out.println(msg.getFileUrl());
                    boolean isMine =
                            msg.getSenderId().equals(
                                    SessionManager.getCurrentUser().getUserId()
                            );

                    if (msg.getFileUrl() != null) {

                        try {

                            String imageUrl = ApiClient.getBaseUrl() + msg.getFileUrl();
                            System.out.println(imageUrl);

                            ImageIcon icon = new ImageIcon(new URL(imageUrl));
                            Image img = icon.getImage().getScaledInstance(220, -1, Image.SCALE_SMOOTH);

                            JLabel imageLabel = new JLabel(new ImageIcon(img));

                            JPanel wrapper = new JPanel(new FlowLayout(
                                    isMine ? FlowLayout.RIGHT : FlowLayout.LEFT
                            ));

                            wrapper.add(imageLabel);

                            messageContainer.add(wrapper);

                        } catch (MalformedURLException e) {
                                System.out.println("Image load error: " + e.getMessage());

                        }

                    } else {

                        MessageBubble bubble =
                                new MessageBubble(msg.getContent(), isMine);

                        messageContainer.add(bubble);

                    }
                }

                        messageContainer.revalidate();
                        messageContainer.repaint();

                        // 👇 auto scroll xuống tin nhắn mới nhất
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            javax.swing.JScrollBar vertical
                                    = JScrollPane.getVerticalScrollBar();
                            vertical.setValue(vertical.getMaximum());
                        });

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
        // TODO add your handling code here:
        File file = chooseFile();

        if (file == null) {
            return;
        }

        MessageType type = detectFileType(file);

        MessageService service = new MessageService();
//        MiMi viết thêm hàm sendFile giúp cp nhé

      service.sendFile(currentChatUserId, file)
.thenAccept(msg -> {

    javax.swing.SwingUtilities.invokeLater(() -> {

        MessageService messageService = new MessageService();

        messageService.getConversation(currentChatUserId)
            .thenAccept(messages -> {

                javax.swing.SwingUtilities.invokeLater(() -> {

                    messageContainer.removeAll();

                    for (Message m : messages) {

                        boolean isMine =
                            m.getSenderId().equals(
                                SessionManager.getCurrentUser().getUserId()
                            );

                        if (m.getMessageType() == MessageType.IMAGE) {

                            try {

                                String imageUrl = ApiClient.getBaseUrl() + m.getFileUrl();

                                ImageIcon icon = new ImageIcon(new URL(imageUrl));
                                Image img = icon.getImage()
                                        .getScaledInstance(220, -1, Image.SCALE_SMOOTH);

                                JLabel imageLabel =
                                        new JLabel(new ImageIcon(img));

                                JPanel wrapper =
                                        new JPanel(new FlowLayout(
                                            isMine ? FlowLayout.RIGHT : FlowLayout.LEFT
                                        ));

                                wrapper.add(imageLabel);

                                messageContainer.add(wrapper);

                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }

                        } else {

                            MessageBubble bubble =
                                    new MessageBubble(m.getContent(), isMine);

                            messageContainer.add(bubble);
                        }
                    }

                    messageContainer.revalidate();
                    messageContainer.repaint();

                });

            });

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
    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        String text = txtMessage.getText().trim();

        if (text.isEmpty()) {
            return;
        }

        MessageService service = new MessageService();

        service.sendMessage(currentChatUserId, text)
                .thenAccept(msg -> {

                    javax.swing.SwingUtilities.invokeLater(() -> {

                        MessageBubble bubble
                                = new MessageBubble(msg.getContent(), true);

                        messageContainer.add(bubble);
                        messageContainer.revalidate();
                        messageContainer.repaint();

                        txtMessage.setText("");

                        JScrollPane.getVerticalScrollBar()
                                .setValue(JScrollPane.getVerticalScrollBar().getMaximum());
                    });

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
