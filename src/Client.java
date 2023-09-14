import java.io.DataInputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 7000);
        DataInputStream dis = new DataInputStream(socket.getInputStream());

        while (true) {
            String time = dis.readUTF();
            System.out.println("Thời gian từ máy chủ: " + time);
        }
    }
}
