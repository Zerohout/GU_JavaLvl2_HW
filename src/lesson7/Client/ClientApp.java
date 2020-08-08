package lesson7.Client;

import lesson7.Helpers.ChatFrameBase;
import lesson7.Message.MessageBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;
import static lesson7.Helpers.ChatCommandsHelper.*;
import static lesson7.Message.MessageBuilder.connectWords;

public class ClientApp extends ChatFrameBase {
    private static int clientCounter;
    private boolean isStopped;
    private DataOutputStream out;
    private DataInputStream in;
    private Socket socket;

    public ClientApp(String host, int port, boolean autoAuth) {
        try {
            this.socket = new Socket(host, port);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            clientCounter++;
            prepareGUI(socket);
            new Thread(this::readMessage).start();
            doAutoAuth(autoAuth, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readMessage() {
        while (!socket.isClosed()) {
            try {
                var text = in.readUTF();
                var parts = text.split("\\s");

                if (parts[1].startsWith("/")) {
                    executeCommand(parts);
                    continue;
                }
                sendLocalMessage(text);
            } catch (IOException ex) {
                ex.printStackTrace();
                return;
            }
        }
    }

    @Override
    protected synchronized void sendMessage(String text) {
        if (text == null || text.isBlank() || text.isEmpty()) return;
        try {
            if(!text.split("\\s")[0].equals(getTitle())){
                text = String.format("%s %s", getTitle(), text);
            }
            out.writeUTF(text);
        } catch (IOException ex) {
            ex.printStackTrace();
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        } finally {
            msgInputField.setText("");
            msgInputField.grabFocus();
        }
    }

    private void executeCommand(String[] parts) throws IOException {
        switch (parts[1]) {
            case END:
                isStopped = true;
                socket.close();
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                break;
            case AUTH_OK:
                updateTitle(parts[2]);
                break;
            default:
                break;
        }
    }

    // for debugging
    private void doAutoAuth(boolean isAutoAuth, int port) {
        if (!isAutoAuth || clientCounter == 1) return;
        var login = "login" + clientCounter;
        var password = "pass" + clientCounter;
        var nickname = "Client" + clientCounter;

        var builder = new MessageBuilder();
        sendMessage(builder.compositeMessage(connectWords(getTitle(), REG, login, password, nickname)).build().getConnectedText());
        sendMessage(builder.compositeMessage(connectWords(getTitle(), AUTH, login, password)).build().getConnectedText());
    }

    //region GUI methods
    private void updateTitle(String title) {
        setVisible(false);
        setTitle(title);
        setVisible(true);
    }

    private synchronized void prepareGUI(Socket socket) {
        setTitle(socket.toString());
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setSize(getFrameSize());
        setLocation(getFrameLocation());

        addComponents();
        setVisible(true);
    }

    private static Dimension getFrameSize() {
        var screenSize = getLocalGraphicsEnvironment().getScreenDevices()[0].getDefaultConfiguration().getBounds().getSize();
        return new Dimension(screenSize.width / 3 - 50, screenSize.height / 2 - 50);
    }

    private Point getFrameLocation() {
        var screenSize = getLocalGraphicsEnvironment().getScreenDevices()[0].getDefaultConfiguration().getBounds().getSize();
        return new Point(screenSize.width - getFrameSize().width - 50,
                (screenSize.height / 2) - (getFrameSize().height / 2));
    }

    private void addComponents() {
        this.chatArea = new JTextArea();
        this.msgInputField = new JTextField();
        addChatArea(getContentPane(), chatArea);
        addBottomPanel(this);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (!isStopped) {
                    isStopped = true;
                    sendMessage("/end");
                }
            }
        });
    }
    //endregion
}
