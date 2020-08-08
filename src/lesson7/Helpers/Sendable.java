package lesson7.Helpers;

import lesson7.Message.Message;

public interface Sendable {
    String getName();
    void sendMessage(Message msg);
    void sendLocalMessage(Message msg);
}
