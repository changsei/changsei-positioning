package server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import mariadb.MariaDBManager;


public class TestServer extends Thread {
	private InputStreamReader fromClient;
	private static Socket socket;
	private static ServerSocket svrSocket;
	
    public TestServer() {
        createStream();
    }

    public static void main(String[] args) {
        try {
            svrSocket = new ServerSocket(8080);
            System.out.println("Waiting for Client");

            while (true) {
                socket = svrSocket.accept();
                System.out.println("Client connection successful");
                TestServer stream = new TestServer();
                stream.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server Closed");
    }

    private void createStream() {
        try {
            fromClient = new InputStreamReader(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        MariaDBManager dbManager = new MariaDBManager();
        dbManager.connectDB();

        try {
            char[] buffer = new char[1024];
            int count;

            while ((count = fromClient.read(buffer)) != -1) {
                String message = new String(buffer, 0, count);
                // 임시로 콘솔에서 출력
                System.out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dbManager.disconnectDB();
                socket.close();
//                boolean check = socket.isClosed();
                fromClient.close();
                System.out.println("Terminate client connection");
            } catch (IOException e) {}
        }
    }
}
