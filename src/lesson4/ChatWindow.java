package lesson4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatWindow extends JFrame {
    private JTextField userMessageField = new JTextField();
    private JTextArea mainMessagesArea = new JTextArea();

    public ChatWindow(String title) {
        super(title);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);

        addComponents();
        setVisible(true);
    }

    private void addComponents() {
        addChatTextArea();
        addUserTextField();
    }

    private void addChatTextArea() {
        mainMessagesArea.setEditable(false);
        mainMessagesArea.setLineWrap(true);
        var messagesFieldPanel = new JPanel(new BorderLayout());
        messagesFieldPanel.add(new JScrollPane(mainMessagesArea));
        add(messagesFieldPanel, BorderLayout.CENTER);
    }

    private void addUserTextField() {
        var userMessageFieldPanel = new JPanel(new BorderLayout());
        userMessageFieldPanel.setPreferredSize(new Dimension(100, 50));
        userMessageFieldPanel.add(new JScrollPane(userMessageField), BorderLayout.CENTER);
        userMessageFieldPanel.add(createSendButton(), BorderLayout.EAST);
        addKeyListenerToJField();

        add(userMessageFieldPanel, BorderLayout.SOUTH);
    }

    private JButton createSendButton() {
        var sendBtn = new JButton("Send");
        sendBtn.setPreferredSize(new Dimension(100, 50));
        sendBtn.addActionListener(actionEvent -> sendMessage());
        return sendBtn;
    }

    private void addKeyListenerToJField() {
        userMessageField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });
    }

    private void sendMessage() {
        if (userMessageField.getText().isEmpty() || userMessageField.getText().isBlank()) return;
        if (!mainMessagesArea.getText().isBlank() || !mainMessagesArea.getText().isEmpty())
            mainMessagesArea.append("\n");

        mainMessagesArea.append("Me: ");
        mainMessagesArea.append(userMessageField.getText());
        userMessageField.setText("");
    }
}
