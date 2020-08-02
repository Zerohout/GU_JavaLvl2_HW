package lesson6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApp extends ChatBase {
    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public ServerApp() {
        prepareGUI();
    }

    private void prepareGUI() {
        setTitle("Сервер");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = sSize.height;
        int width = sSize.width;
        setSize(width / 3, height / 2);
        setLocation((width / 3) / 4, (height / 2) / 2);

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

    public void startServerApp() throws IOException {
        serverSocket = new ServerSocket(8189);

        chatArea.setText("Сервер запущен, ожидаем подключения...\n");
        socket = serverSocket.accept();
        chatArea.append(String.format("Клиент %s подключился\n", socket.toString()));
        chatArea.append("Для разрыва соединения и выхода, введите команду \"\\end\"\nили просто закройте окно\n");
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        listeningClient();
    }

    private void listeningClient() {
        new Thread(() -> {
            try {
                while (true) {
                    String strFromClient = in.readUTF();
                    if (strFromClient.equalsIgnoreCase("/end")) {
                        out.writeUTF(strFromClient);
                        closeConnection();
                        break;
                    }
                    chatArea.append("Клиент: ");
                    chatArea.append(strFromClient);
                    chatArea.append("\n");
                }
            } catch (IOException ex) {
                ex.getStackTrace();
                closeConnection();
            }
        }).start();
    }

    private void closeConnection() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
