package test;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class MyServer {
    private int port;
    private ClientHandler ch;
    private volatile boolean stop;

    public MyServer(int port, ClientHandler ch) {
        this.port = port;
        this.ch = ch;
        stop = false;
    }

    private void runServer() {
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            server.setSoTimeout(1000);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        while (!stop) {
            try {
                Socket aClient = server.accept();
                try {
                    ch.handleClient(aClient.getInputStream(), aClient.getOutputStream());
                    aClient.getInputStream().close();
                    aClient.getOutputStream().close();
                    aClient.close();
                } catch (IOException e) {
                } finally {
                    aClient.getInputStream().close();
                    aClient.getOutputStream().close();
                    aClient.close();
                }
            } catch (IOException e) {
            }
        }
        try {
            server.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        new Thread(() -> runServer()).start();
    }


    public void close() {
        stop = true;
    }
}
