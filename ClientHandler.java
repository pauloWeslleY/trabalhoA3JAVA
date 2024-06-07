import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            out.println("Welcome to Rock-Paper-Scissors! You are now connected.");

            while (true) {
                out.println("YOUR TURN");
                String clientChoice = in.readLine();

                if (clientChoice == null) {
                    break;
                }

                String serverChoice = getRandomChoice();
                out.println("Server's choice: " + serverChoice);

                // Here you can implement the logic to determine the winner
                // based on clientChoice and serverChoice

                // For simplicity, let's just send a random message
                out.println("You win!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getRandomChoice() {
        String[] choices = {"rock", "paper", "scissors"};
        Random random = new Random();
        int index = random.nextInt(choices.length);
        return choices[index];
    }
}
