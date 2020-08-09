package lesson7_8.Server;

import lesson7_8.Message.Message;
import lesson7_8.Message.MessageBuilder;

import static lesson7_8.Helpers.ChatCommandsHelper.*;
import static lesson7_8.Message.MessageBuilder.*;
import static lesson7_8.Server.ServerHandler.SERVER_NAME;

public class ServerCommandHandler {
    private ServerHandler server;
    private MessageBuilder mb;

    public ServerCommandHandler(ServerHandler server) {
        mb = new MessageBuilder();
        this.server = server;
    }

    synchronized void serverCommandListener(Message msg) {
        switch (msg.getCommand()) {
            case HELP -> executeHelpCommand(msg);
            case GET_COMMS -> executeGetCommsCommand();
            case AUTH, REG, LOGOUT -> executeUnneededCommands();
            case PRIVATE_MSG -> executePrivateMsgCommand(msg);
            case GET_ONLINE -> executeGetOnline();
            case END -> executeEndCommand();
            default -> sendServerSystemMessage("Incorrect command");
        }
    }

    // SERVER /help
    // SERVER /help (0)/command
    private void executeHelpCommand(Message msg) {
        var args = msg.getCommandArgs();
        sendServerSystemMessage(getCommandHelp(args.length == 0 ? msg.getCommand() : args[0]));
    }

    // SERVER /getcomms
    private void executeGetCommsCommand() {
        sendServerSystemMessage(getAllCommands().toString());
    }

    private void executeUnneededCommands() {
        sendServerSystemMessage("You don't need this commands");
    }

    // SERVER /t (0)recipient (1...)text
    // SERVER /t (0)recipient (1)/command (2...)args
    // SERVER /t (0)recipient (1)/t (2)/recipient (3...)text
    private void executePrivateMsgCommand(Message msg) {
        var args = msg.getCommandArgs();
        if (args.length < 2) {
            sendServerSystemMessage("Incorrect command");
            return;
        }
        var recipient = ServerHandler.getClientByNickname(args[0]);
        if (recipient == null) {
            sendServerSystemMessage("Nickname not found");
            return;
        }
        if (args[1].startsWith("/")) {
            mb.reset().compositeMessage(connectParts(args, 0)).setRecipients(recipient).build().send();
        } else {
            mb.reset().isCommand(true).isPrivate(true).setSender(SERVER_NAME)
                    .setRecipients(server, recipient).setText(connectParts(args, 1)).build().send();
        }
    }

    private void executeGetOnline() {
        sendServerSystemMessage("Online users: \n" + server.getNicknames());
    }

    private void executeEndCommand() {
        server.stopServer();
    }

    private void sendServerSystemMessage(String text) {
        mb.reset().setServerSystemMessage(text).setRecipients(server).build().send();
    }
}

