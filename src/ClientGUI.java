import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;

public class ClientGUI extends JFrame {
    private JLabel timeLabel;
    private Timer timer;
    private JButton runBtn;
    private JButton stopBtn;
    private Socket socket;
    private DataOutputStream dos;

    public ClientGUI() {
        setTitle("Đồng hồ thời gian");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 36));
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        getContentPane().add(timeLabel, BorderLayout.CENTER);

        // Tạo và khởi động timer để cập nhật thời gian mỗi giây
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateTime();
            }
        });
        timer.start();

        // Tạo phím run
        runBtn = new JButton("Run");
        runBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startTimer();
                sendRequestToServer("START_TIMER");
            }
        });

        stopBtn = new JButton("Stop");
        stopBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopTimer();
                sendRequestToServer("STOP_TIMER");
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(runBtn);
        buttonPanel.add(stopBtn);

        JPanel timePanel = new JPanel();
        timePanel.setLayout(new BorderLayout());
        timePanel.add(timeLabel, BorderLayout.CENTER);
        timePanel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(timePanel, BorderLayout.CENTER);

        // Kết nối tới máy chủ
        try {
            socket = new Socket("localhost", 7000);
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void startTimer() {
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    private void stopTimer() {
        if (timer.isRunning()) {
            timer.stop();
        }
    }

    private void updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(new java.util.Date());
        timeLabel.setText(time);
    }

    private void sendRequestToServer(String request) {
        try {
            dos.writeUTF(request);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientGUI clockApp = new ClientGUI();
            clockApp.setVisible(true);
        });
    }
}
