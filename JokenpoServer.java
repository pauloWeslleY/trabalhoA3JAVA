import java.io.*;
import java.net.*;

public class JokenpoServer {
  private static final int PORT = 12345;

  public static void main(String[] args) throws IOException {
    ServerSocket serverSocket = new ServerSocket(PORT);
    System.out.println("Servidor iniciado. Aguardando jogadores...");

    try {
      Socket player1Socket = serverSocket.accept();
      System.out.println("Jogador 1 conectado.");
      Socket player2Socket = serverSocket.accept();
      System.out.println("Jogador 2 conectado.");

      new Thread(new GameHandler(player1Socket, player2Socket)).start();
    } finally {
      serverSocket.close();
    }
  }
}

class GameHandler implements Runnable {
  private Socket player1Socket;
  private Socket player2Socket;

  public GameHandler(Socket player1Socket, Socket player2Socket) {
    this.player1Socket = player1Socket;
    this.player2Socket = player2Socket;
  }

  @Override
  public void run() {
    try {
      BufferedReader player1In = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
      PrintWriter player1Out = new PrintWriter(player1Socket.getOutputStream(), true);
      BufferedReader player2In = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));
      PrintWriter player2Out = new PrintWriter(player2Socket.getOutputStream(), true);

      player1Out.println("Bem-vindo Jogador 1! Aguarde o Jogador 2.");
      player2Out.println("Bem-vindo Jogador 2!");

      player1Out.println("Faça sua jogada (PEDRA, PAPEL ou TESOURA): ");
      String move1 = player1In.readLine().toUpperCase();

      player2Out.println("Faça sua jogada (PEDRA, PAPEL ou TESOURA): ");
      String move2 = player2In.readLine().toUpperCase();

      String result = determineWinner(move1, move2);

      player1Out.println("Jogador 2 jogou: " + move2);
      player2Out.println("Jogador 1 jogou: " + move1);

      player1Out.println(result);
      player2Out.println(result);

      player1Socket.close();
      player2Socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String determineWinner(String move1, String move2) {
    if (move1.equals(move2)) {
      return "Empate!";
    } else if ((move1.equals("PEDRA") && move2.equals("TESOURA")) ||
        (move1.equals("PAPEL") && move2.equals("PEDRA")) ||
        (move1.equals("TESOURA") && move2.equals("PAPEL"))) {
      return "Jogador 1 vence!";
    } else {
      return "Jogador 2 vence!";
    }
  }
}
