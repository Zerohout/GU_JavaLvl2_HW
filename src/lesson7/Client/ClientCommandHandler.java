package lesson7.Client;

import lesson7.Message.Message;
import lesson7.Message.MessageBuilder;
import lesson7.Server.ServerHandler;

import java.io.IOException;

import static lesson7.Helpers.ChatCommandsHelper.*;
import static lesson7.Message.MessageBuilder.connectParts;

public class ClientCommandHandler {

    private ClientHandler client;
    private ServerHandler server;
    private MessageBuilder mb;

    public ClientCommandHandler(ClientHandler client, ServerHandler server) {
        this.client = client;
        this.server = server;
        mb = new MessageBuilder();
    }

    public synchronized void commandListener(Message msg) {
        switch (msg.getCommand()) {
            case HELP -> executeHelpCommand(msg);
            case GET_COMMS -> executeGetCommsCommand();
            case AUTH, REG -> executeUnneededCommand();
            case AUTH_OK -> executeAuthOkCommand(msg);
            case LOGOUT -> executeLogoutCommand();
            case PRIVATE_MSG -> executePrivateMsgCommand(msg);
            case END -> executeEndCommand();
            case GET_ONLINE -> executeGetOnlineCommand();
            default -> executeDefaultCommand();
        }
    }

    // sender /help
    // sender /help (0)arg
    private void executeHelpCommand(Message msg) {
        var args = msg.getCommandArgs();
        sendServerSystemMessage(getCommandHelp(args.length == 0 ? msg.getCommand() : args[0]));
    }


    private void executeGetCommsCommand() {
        sendServerSystemMessage(getAllCommands().toString());
    }


    private void executeUnneededCommand() {
        sendServerSystemMessage("You don't need this commands");
    }

    // sender /authok (0)nickname
    private void executeAuthOkCommand(Message msg) {
        mb.convertMsgToBuilder(msg).isSystem(true).setRecipients(client)
                .setText(msg.getText()).build().send();
        server.subscribe(client);
    }

    private void executeLogoutCommand() {
        server.unsubscribe(client);
    }

    // sender /t (0)recipient (1...)text
    private void executePrivateMsgCommand(Message msg) {
        var recipient = ServerHandler.getClientByNickname(msg.getCommandArgs()[0]);
        if (recipient == null) {
            sendServerSystemMessage("User not found");
            return;
        }
        mb.reset().setSender(client.getName()).isCommand(true).isPrivate(true).addRecipients(client, recipient)
                .setText(connectParts(msg.getCommandArgs(),1)).build().send();
    }

    private void executeEndCommand() {
        try {
            server.closeClient(client);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void executeGetOnlineCommand() {
        sendServerSystemMessage("Online users: \n" + server.getNicknames());
    }

    private void executeDefaultCommand() {
        sendServerSystemMessage("Incorrect command");
    }

    private void sendServerSystemMessage(String text) {
        mb.reset().setServerSystemMessage(text).setRecipients(client).build().send();
    }
}
