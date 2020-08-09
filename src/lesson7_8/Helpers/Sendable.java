package lesson7_8.Helpers;

import lesson7_8.Message.Message;

public interface Sendable {
    String getName();
    void sendMessage(Message msg);
    void sendLocalMessage(Message msg);
}
