import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PlayerVsPlayerClient {
    public static void main(String[] args) {
        try {
            @SuppressWarnings("resource")
            Socket socket = new Socket("localhost", 5000);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            String response;
            while ((response = in.readLine()) != null) {
                System.out.println("Server: " + response);
                if (response.equals("YOUR TURN")) {
                    System.out.println("Enter your choice (rock, paper, or scissors): ");
                    String choice = userInput.readLine();
                    out.println(choice);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

