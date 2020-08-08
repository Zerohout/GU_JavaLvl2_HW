package lesson7.AuthService;

import lesson7.Client.ClientHandler;
import lesson7.Message.Message;
import lesson7.Message.MessageBuilder;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static lesson7.Helpers.ChatCommandsHelper.*;
import static lesson7.Message.MessageBuilder.*;
import static lesson7.Helpers.ControlPanel.getCurrentServer;

public class AuthService {
    private int clientIdleTime = 120;
    private int waitingClientStep = 20;
    private ArrayList<User> users;
    public static final String AUTH_SERVICE_NAME = "Auth_Service_1.0";

    private final Object monitor1 = new Object();

    public void initUsers() {
        this.users = new ArrayList<>();
        for (var i = 1; i <= 2; i++) {
            users.add(new UserBuilder().setLogin("login" + i).setPassword("pass" + i).setNickname("Client" + i).build());
        }
    }

    public void start() {
        sendMessageToServer("started and ready to work.");
        initUsers();
    }

    public void stop() {
        sendMessageToServer("stopped.");
        users.clear();
    }

    public void clientAuthentication(Message msg, ClientHandler client) {
        if (!msg.isCommand()) {
            sendMessageToClient("You cannot send messages while not get authorization.", client);
            return;
        }
        synchronized (monitor1) {
            switch (msg.getCommand()) {
                case AUTH -> doAuthentication(msg.getCommandArgs(), client);
                case REG -> doRegistration(msg.getCommandArgs(), client);
                default -> sendMessageToClient("Incorrect data. Please try again", client);
            }
        }
    }

    private void doAuthentication(String[] args, ClientHandler client) {
        if (args.length != 2) {
            sendMessageToClient("Incorrect data. Please try again", client);
            return;
        }
        var user = getUserByLoginPass(args[0], args[1]);
        if (user == null) {
            sendMessageToClient("Incorrect login and/or password", client);
            return;
        }

        if (user.isOnline()) {
            sendMessageToClient(String.format("Nickname[%s] is already in use", user.getNickname()), client);
        } else {
            client.setUser(user);
            client.isAuth(true);
            new MessageBuilder().compositeMessage(connectWords(AUTH_OK, user.getNickname())).addRecipients(client).build().send();
        }
    }

    private void doRegistration(String[] args, ClientHandler client) {
        if (args.length != 3) {
            sendMessageToClient("Incorrect data. Please try again", client);
            return;
        }
        String text;
        if (insertUser(args)) {
            text = String.format("Registration is done. Please get authorization by command \"%s login pass\"", AUTH);
        } else {
            text = String.format("Registration is not complete. Please try again or get authorization by command \"%s login pass\"", AUTH);
        }
        sendMessageToClient(text, client);
    }

    private void sendMessageToClient(String text, ClientHandler client) {
        new MessageBuilder().setAuthSystemMessage(text).setRecipients(client).build().send();
    }

    private void sendMessageToServer(String text) {
        new MessageBuilder().setAuthSystemMessage(text).setRecipients(getCurrentServer()).build().send();
    }

    public User getUserByNickname(String nickname) {
        for (var user : users) {
            if (user.isNicknameCorrect(nickname)) return user;
        }
        return null;
    }

    private User getUserByLogin(String login) {
        for (var user : users) {
            if (user.isLoginCorrect(login)) return user;
        }
        return null;
    }

    private User getUserByLoginPass(String login, String password) {
        for (var user : users) {
            if (user.isLoginPassCorrect(login, password)) return user;
        }
        return null;
    }

    private boolean insertUser(String[] args) {
        if (getUserByLogin(args[0]) == null && getUserByNickname(args[2]) == null) {
            users.add(new UserBuilder().setLogin(args[0]).setPassword(args[1]).setNickname(args[2]).build());
            return true;
        }
        return false;
    }

    public void setClientIdleWatcher(ClientHandler client) {
        var text = String.format("""
                Please get authorization by command "%s login password"
                or get registration by command "%s login password nickname\"""", AUTH, REG);
        sendMessageToClient(text, client);
        var timer = new Timer(true);
        var timerAction = new TimerAction(client, timer);
        timer.schedule(timerAction, 0, waitingClientStep * 1000);
    }

    private class TimerAction extends TimerTask {
        private ClientHandler client;
        private int timerCount;
        private Timer timer;

        public TimerAction(ClientHandler client, Timer timer) {
            this.client = client;
            this.timer = timer;
        }

        @Override
        public void run() {
            if (client == null || !client.isOnline() || client.isAuth()) {
                timer.cancel();
            } else {
                if (timerCount >= clientIdleTime) {
                    new MessageBuilder().compositeMessage(END).setRecipients(client).build().send();
                    timer.cancel();
                } else {
                    var text = String.format("""
                    There are %d seconds left until the end of authentication.
                    After the time expires, you will be forcibly disconnected from the server.""",
                            clientIdleTime - timerCount);
                    sendMessageToClient(text, client);
                }
            }
            timerCount += waitingClientStep;
        }
    }
}
