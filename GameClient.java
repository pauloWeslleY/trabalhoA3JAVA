import java.io.*;
import java.net.*;

public class GameClient {
  private static final String SERVER_ADDRESS = "192.168.100.3";
  private static final int SERVER_PORT = 12345;

  public static void main(String[] args) {
    try {
      Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

      String response;
      while ((response = in.readLine()) != null) {
        System.out.println(response);
        if (response.startsWith("Vitórias") || response.equals("O oponente saiu do jogo.")) {
          break;
        }
        String input = console.readLine();
        if (input != null) {
          out.println(input);
        }
      }
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
