package lesson6;
import java.io.IOException;

public class Lesson6 {
    public void start() {
        var server = new Thread(this::startServer);
        var client = new Thread(this::startClient);
        server.start();

        while (!server.isAlive()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        client.start();
    }

    private void startServer() {
        var server = new ServerApp();
        try {
            server.startServerApp();
        } catch (IOException ex) {
            ex.printStackTrace();
            server.dispose();
        }
    }

    private void startClient() {
        var client = new ClientApp();
        try {
            client.startClientApp();
        } catch (IOException ex) {
            ex.printStackTrace();
            client.dispose();
        }
    }
}
