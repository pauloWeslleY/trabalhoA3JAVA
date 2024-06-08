package jogo1;

import java.io.*;
import java.net.*;

public class GameClient {

  public static void main(String[] args) throws IOException {
    BufferedReader userIP = new BufferedReader(new InputStreamReader(System.in));
    String IP = userIP.readLine();

    try {
      Socket socket = new Socket(IP, 12345);
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
