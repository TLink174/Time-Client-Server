import java.io.*;
import java.net.*;
import java.util.Date;

public class Server {

    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(7000);
        System.out.println("Server is started");

        while (true) {
            Socket socket = server.accept();
            new ClientHandler(socket).start();
            new ClientHandler.TimerThread().start();
        }
    }
}
