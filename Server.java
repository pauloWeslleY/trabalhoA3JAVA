import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            @SuppressWarnings("resource")
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server is running...");
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {   
            e.printStackTrace();
        }
    }
}
