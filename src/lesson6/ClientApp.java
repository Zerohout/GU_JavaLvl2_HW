package lesson6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientApp extends ChatBase {
    private final String SERVER_ADDR = "localhost";
    private final int SERVER_PORT = 8189;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public ClientApp() {
        prepareGUI();
    }

    private void prepareGUI() {
        setTitle("Клиент");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = sSize.height;
        int width = sSize.width;
        setSize(width / 3, height / 2);
        setLocation((width / 3) / 2 + (width / 3), (height / 2) / 2);

        addComponents();

        setVisible(true);
    }

    @Override
    protected void addWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    out.writeUTF("/end");
                    closeConnection();
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void sendMessage() {
        if (!msgInputField.getText().trim().isEmpty()) {
            try {
                out.writeUTF(msgInputField.getText());
                addTextToChatArea();
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Ошибка отправки сообщения");
            }
        }
    }

    public void startClientApp() throws IOException {
        openConnection();
        new Thread(() -> {
            try {
                while (true) {
                    String strFromServer = in.readUTF();
                    if (strFromServer.equalsIgnoreCase("/end")) {
                        closeConnection();
                        break;
                    }
                    chatArea.append("Сервер: ");
                    chatArea.append(strFromServer);
                    chatArea.append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void openConnection() throws IOException {
        socket = new Socket(SERVER_ADDR, SERVER_PORT);
        chatArea.append("Совершено подключение к серверу.\nДля разрыва соединения и выхода, введите команду \"\\end\"\nили просто закройте окно\n");
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    private void closeConnection() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
