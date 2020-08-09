package lesson7_8.Server;

import lesson7_8.AuthService.AuthService;
import lesson7_8.Client.ClientHandler;
import lesson7_8.Helpers.Sendable;
import lesson7_8.Message.Message;
import lesson7_8.Message.MessageBuilder;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

import static lesson7_8.Helpers.ChatCommandsHelper.*;
import static lesson7_8.Message.MessageBuilder.*;

public class ServerHandler implements Sendable {
    private final int port;
    private ServerSocket serverSocket;
    private ServerApp serverApp;
    private AuthService authService;
    private static HashSet<ClientHandler> onlineClients = new HashSet<>();
    private HashSet<ClientHandler> notAuthClients;
    private ServerCommandHandler commandHandler;
    public final static String SERVER_NAME = "SERVER";
    private boolean isStopped;
    private MessageBuilder mb;
    private boolean isSystemFlag;

    @Override
    public String getName() {
        return SERVER_NAME;
    }

    public AuthService getAuthService() {
        return this.authService;
    }

    public static ClientHandler getClientByNickname(String nickname) {
        for (var client : onlineClients) {
            if (client.isNicknameCorrect(nickname)) return client;
        }
        return null;
    }

    public ArrayList<String> getNicknames() {
        var out = new ArrayList<String>();
        for (var client : onlineClients) {
            out.add(client.getName());
        }
        return out;
    }

    public ServerHandler(int port, ServerApp serverApp) {
        this.mb = new MessageBuilder();
        this.port = port;
        this.notAuthClients = new HashSet<>();
        this.serverApp = serverApp;
        new Thread(this::start).start();
    }

    private void start() {
        try (ServerSocket server = new ServerSocket(this.port)) {
            this.serverSocket = server;
            authService = new AuthService();
            authService.start();
            commandHandler = new ServerCommandHandler(this);
            serverApp.sendLocalMessage("Server started on port: " + port);
            while (!server.isClosed() && !isStopped) {
                Socket socket = server.accept();
                serverApp.sendLocalMessage(String.format("Client connected on %s", socket.toString()));
                var client = new ClientHandler(this, socket);
                addNotAuthClient(client);
            }
        } catch (Exception ex) {
            // ex.printStackTrace();
        } finally {
            if (!isStopped) {
                closeClients();
                if (serverApp != null) serverApp.close();
            }
        }
    }

    public void stopServer() {
        authService.stop();
        isStopped = true;
        closeClients();
        serverApp.close();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //region sending messages
    public synchronized void broadcastMessage(Message msg, boolean isSystem) {
        mb.convertMsgToBuilder(msg).isSystem(isSystem)
                .setRecipients(new ArrayList<>(onlineClients))
                .addRecipients(this).build().sendLocal();
    }

    @Override
    public void sendMessage(Message msg) {
        if (msg.isCommand()) {
            commandHandler.serverCommandListener(msg);
            return;
        }

        broadcastMessage(msg,false);
    }

    @Override
    public void sendLocalMessage(Message msg) {
        serverApp.sendLocalMessage(msg.getConnectedText());
    }
    //endregion

    //region Clients methods
    public synchronized void subscribe(ClientHandler client) {
        notAuthClients.remove(client);
        onlineClients.add(client);
        broadcastMessage(mb.reset().setServerSystemMessage(connectWords(client.getName(), "come in chat.")).build(),true);
    }

    public synchronized void unsubscribe(ClientHandler client) {
        onlineClients.remove(client);
        client.isAuth(false);
        addNotAuthClient(client);
        broadcastMessage(mb.reset().setServerSystemMessage(connectWords(client.getName(), "left the chat.")).build(),true);
    }

    private void addNotAuthClient(ClientHandler client) {
        notAuthClients.add(client);
        authService.setClientIdleWatcher(client);
    }

    private void closeClients() {
        mb = mb.reset().compositeMessage(END);
        mb.setRecipients(new ArrayList<>(notAuthClients)).build().send();
        mb.setRecipients(new ArrayList<>(onlineClients)).build().send();
    }

    public void closeClient(ClientHandler client) throws IOException {
        onlineClients.remove(client);
        notAuthClients.remove(client);
        if (client.isAuth()) {
            broadcastMessage(mb.reset().setServerSystemMessage(connectWords(client.getName(), "left the chat")).build(),true);
        } else {
            mb.reset().setServerSystemMessage(connectWords(client.getName(), "disconnected"))
                    .setRecipients(this).build().send();
        }
        client.closeClientHandler();
    }
    //endregion
}

