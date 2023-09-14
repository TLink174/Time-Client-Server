import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.Date;

public class ServerGUI extends JFrame {
    private JTextArea logArea;
    private ServerSocket serverSocket;

    public ServerGUI() {
        setTitle("Máy Chủ Thời Gian");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        JButton startButton = new JButton("Bắt đầu máy chủ");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startServer();
                startButton.setEnabled(false);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void startServer() {
        try {
            serverSocket = new ServerSocket(12345);
            log("Đang chờ kết nối từ máy khách...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                log("Kết nối từ " + clientSocket.getInetAddress().getHostAddress());

                OutputStream os = clientSocket.getOutputStream();
                PrintWriter out = new PrintWriter(os, true);

                Date currentTime = new Date();
                out.println("Thời gian hiện tại từ máy chủ: " + currentTime);

                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void log(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ServerGUI serverGUI = new ServerGUI();
            serverGUI.setVisible(true);
        });
    }
}
