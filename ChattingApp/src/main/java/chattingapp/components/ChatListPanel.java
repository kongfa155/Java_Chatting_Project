/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package chattingapp.components;

import chattingapp.models.ChatData;
import chattingapp.models.Message;
import chattingapp.models.User;
import chattingapp.services.FriendService;
import chattingapp.dtos.FriendLoadDTO;

/**
 *
 * @author CP
 */
public class ChatListPanel extends javax.swing.JPanel {

    private ChatItemPanel selectedItem;
    private ChatSelectionListener chatSelectionListener;

    /**
     * Creates new form ChatListPanel
     */
    public ChatListPanel() {
        initComponents();
        setupUI();
        loadFriends();
    }
    private java.util.List<ChatData> allChats = new java.util.ArrayList<>();

    //Hàm này sau này dùng để cập nhật lại list khi có tin nhắn mới
    public void receiveNewMessage() {
        loadFriends(); // reload lại list
    }

    private void setupUI() {

        // Search box đẹp hơn
        txtSearch.putClientProperty("JTextField.placeholderText", "Search...");

        txtSearch.setBorder(
                javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 15)
        );

        // Scroll đẹp hơn
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);

        // Container spacing
        listContainer.setBorder(
                javax.swing.BorderFactory.createEmptyBorder(5, 0, 5, 0)
        );

        scrollPane.getVerticalScrollBar().putClientProperty("JScrollBar.showButtons", false);
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                search();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                search();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                search();
            }

            private void search() {
                String keyword = txtSearch.getText().trim();
                filterList(keyword);
            }
        });
    }

    private void loadFriends() {

        FriendService service = new FriendService();

        service.getFriends().thenAccept(friends -> {

            javax.swing.SwingUtilities.invokeLater(() -> {

                listContainer.removeAll();

                allChats.clear(); // reset

                for (FriendLoadDTO f : friends) {

                    User user = new User();
                    user.setUserId(f.getUserId());
                    user.setDisplayName(f.getDisplayName());
                    user.setAvatarUrl(f.getAvatarUrl());

                    Message msg = new Message();
                    msg.setContent("");
                    msg.setSentAt(java.time.LocalDateTime.now());

                    ChatData data = new ChatData(user, msg, 0);

                    allChats.add(data); // ✅ lưu lại

                    ChatItemPanel item = createItem(data);
                    listContainer.add(item);
                }

                listContainer.revalidate();
                listContainer.repaint();
            });

        });
    }

    private ChatItemPanel createItem(ChatData data) {
        ChatItemPanel item = new ChatItemPanel(data);
        FriendService service = new FriendService(); // Tạo instance service

        item.setChatItemClickListener(clickedData -> {
            // Kiểm tra xem friend còn tồn tại không
            service.isFriend(clickedData.getContact().getUserId())
                    .thenAccept(isFriend -> {
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            if (!isFriend) {
                                // Nếu đã bị xóa, thông báo và reload list
                                javax.swing.JOptionPane.showMessageDialog(
                                        this,
                                        clickedData.getContact().getDisplayName() + " đã không còn là bạn.",
                                        "Thông báo",
                                        javax.swing.JOptionPane.WARNING_MESSAGE
                                );
                                
                                 chattingapp.ui.MainFrame.getInstance().getChatPanel().resetChat();
                                receiveNewMessage(); // reload list chat
                                return;
                            }

                            // Nếu còn là bạn, chọn item bình thường
                            if (selectedItem != null) {
                                selectedItem.setSelected(false);
                            }

                            item.setSelected(true);
                            selectedItem = item;

                            if (chatSelectionListener != null) {
                                chatSelectionListener.onChatSelected(clickedData);
                            }
                        });
                    });
        });

        return item;
    }

    private void filterList(String keyword) {
        listContainer.removeAll();

        String lower = keyword.toLowerCase();

        for (ChatData data : allChats) {
            String name = data.getContact().getDisplayName().toLowerCase();

            if (name.contains(lower)) {
                listContainer.add(createItem(data));
            }
        }

        listContainer.revalidate();
        listContainer.repaint();
    }

    //Đây là kênh đàm thoại cấp cao, dùng để cho các quản lý giao tiếp tới nhau
    public interface ChatSelectionListener {

        void onChatSelected(ChatData data);
    }

    public void setChatSelectionListener(ChatSelectionListener listener) {
        this.chatSelectionListener = listener;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtSearch = new javax.swing.JTextField();
        scrollPane = new javax.swing.JScrollPane();
        listContainer = new javax.swing.JPanel();

        setMinimumSize(new java.awt.Dimension(280, 0));
        setPreferredSize(new java.awt.Dimension(300, 0));
        setLayout(new java.awt.BorderLayout());

        txtSearch.setMinimumSize(new java.awt.Dimension(64, 40));
        txtSearch.setPreferredSize(new java.awt.Dimension(71, 45));
        add(txtSearch, java.awt.BorderLayout.PAGE_START);

        listContainer.setLayout(new javax.swing.BoxLayout(listContainer, javax.swing.BoxLayout.Y_AXIS));
        scrollPane.setViewportView(listContainer);

        add(scrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel listContainer;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
