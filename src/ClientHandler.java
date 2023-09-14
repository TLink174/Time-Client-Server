import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Date;

public class ClientHandler extends Thread {
    private static boolean timerRunning = false;

    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            while (true) {
                String clientMessage = dis.readUTF();
                if (clientMessage.equals("START_TIMER")) {
                    startTimer();
                    dos.writeUTF("Timer started.");
                } else if (clientMessage.equals("STOP_TIMER")) {
                    stopTimer();
                    dos.writeUTF("Timer stopped.");
                }else if (clientMessage.equals("EXIT")) {
                    dos.writeUTF("Exiting...");
                    break; // Thoát khỏi vòng lặp khi nhận yêu cầu "EXIT"
                }
            }
        } catch (Exception e) {
            System.err.println("Kết nối bị đóng hoặc lỗi: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private static void startTimer() {
        if (!timerRunning) {
            timerRunning = true;
            new TimerThread().start();
        }
    }

    private static void stopTimer() {
        timerRunning = false;
    }

    public static class TimerThread extends Thread {
        public void run() {
            while (timerRunning) {
                try {
                    Date currentTime = new Date();
                    System.out.println("Current time: " + currentTime);
                    Thread.sleep(1000); // Chờ 1 giây trước khi gửi thời gian tiếp theo
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


