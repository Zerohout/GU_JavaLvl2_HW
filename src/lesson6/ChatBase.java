package lesson6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public abstract class ChatBase extends JFrame {
    protected JTextArea chatArea;
    protected JTextField msgInputField;

    protected void addComponents() {
        addChatArea();
        JPanel bottomPanel = new JPanel(new BorderLayout());
        addMsgInputField(bottomPanel);
        addBtnSendMsg(bottomPanel);
        add(bottomPanel, BorderLayout.SOUTH);
        addWindowListener();
    }

    private void addChatArea() {
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);
    }

    private void addMsgInputField(JPanel panel) {
        msgInputField = new JTextField();
        msgInputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });
        panel.add(msgInputField, BorderLayout.CENTER);
    }

    private void addBtnSendMsg(JPanel panel) {
        JButton btnSendMsg = new JButton("Отправить");
        panel.add(btnSendMsg, BorderLayout.EAST);
        btnSendMsg.addActionListener(e -> sendMessage());
    }

    protected void addTextToChatArea(){
        chatArea.append("Я: ");
        chatArea.append(msgInputField.getText());
        chatArea.append("\n");
        msgInputField.setText("");
        msgInputField.grabFocus();
    }

    protected abstract void sendMessage();
    protected abstract void addWindowListener();
}
