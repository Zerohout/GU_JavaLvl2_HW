package lesson7.Client;

import lesson7.AuthService.UserBuilder;
import lesson7.Helpers.Sendable;
import lesson7.Message.Message;
import lesson7.AuthService.AuthService;
import lesson7.Message.MessageBuilder;
import lesson7.Server.ServerHandler;
import lesson7.AuthService.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static lesson7.Helpers.ChatCommandsHelper.END;
import static lesson7.Message.MessageBuilder.connectWords;

public class ClientHandler implements Sendable {
    transient private ServerHandler server;
    transient private Socket socket;
    transient private AuthService authService;
    transient private ClientCommandHandler commandHandler;
    transient private DataOutputStream out;
    transient private DataInputStream in;
    private boolean isAuth = false;
    private User user;
    private MessageBuilder mb;

    public ClientHandler(ServerHandler server, Socket socket) {
        mb = new MessageBuilder();
        this.server = server;
        this.socket = socket;
        try {
            this.out = new DataOutputStream(socket.getOutputStream());
            this.in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.user = new UserBuilder().setNickname(socket.toString()).build();
        this.authService = server.getAuthService();
        this.commandHandler = new ClientCommandHandler(this, server);
        new Thread(this::readMessage).start();
    }

    private void readMessage() {
        try {
            while (!socket.isClosed()) {
                mb.reset().compositeMessage(in.readUTF()).addRecipients(this).build().send();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void sendMessage(Message msg) {
        if (msg.isSystem() || msg.isPrivate()) sendLocalMessage(msg);
        else if (!isAuth && !msg.getCommand().equals(END)) doAuthentication(msg);
        else if (msg.isCommand()) commandHandler.commandListener(msg);
        else server.broadcastMessage(msg);
    }

    @Override
    public void sendLocalMessage(Message msg) {
        try {
            out.writeUTF(msg.getConnectedText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doAuthentication(Message msg) {
        authService.clientAuthentication(msg, this);
    }

    //region Getters and Setters
    public void setUser(User user) {
        this.user = user;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public void isAuth(boolean isAuth) {
        this.isAuth = isAuth;
    }

    public boolean isOnline() {
        return user.isOnline();
    }

    public boolean isNicknameCorrect(String nickname) {
        return user.isNicknameCorrect(nickname);
    }

    @Override
    public String getName() {
        return this.user.getNickname();
    }
    //endregion

    public void closeClientHandler() throws IOException {
        sendLocalMessage(mb.reset().compositeMessage(END).build());
        in.close();
        out.close();
        socket.close();
        user.isOnline(false);
    }
}
