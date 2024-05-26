import java.io.*;
import java.net.*;

public class JokenpoClient {
  private static final String SERVER_ADDRESS = "localhost";
  private static final int SERVER_PORT = 12345;

  public static void main(String[] args) {
    try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

      System.out.println(in.readLine());

      System.out.println(in.readLine());
      String userInput = stdIn.readLine();
      out.println(userInput);

      System.out.println(in.readLine());
      System.out.println(in.readLine());
      System.out.println(in.readLine());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
