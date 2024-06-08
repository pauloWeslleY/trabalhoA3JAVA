package jogo1;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class GameServer {
  private static final int PORT = 12345;
  private static Set<GameHandler> gameHandlers = ConcurrentHashMap.newKeySet();

  public static void main(String[] args) throws IOException {
    System.out.println("Servidor iniciado...");
    ServerSocket serverSocket = new ServerSocket(PORT);

    try {
      while (true) {
        Socket clientSocket = serverSocket.accept();
        System.out.println("New client connected: " + clientSocket);
        GameHandler handler = new GameHandler(clientSocket);
        gameHandlers.add(handler);
        new Thread(handler).start();
      }
    } finally {
      serverSocket.close();
    }
  }

  private static class GameHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String name;
    private boolean playingAgainstCPU;
    private GameHandler opponent;

    public GameHandler(Socket socket) {
      this.socket = socket;
    }

    public void run() {
      try {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        out.println("Digite seu nome:");
        name = in.readLine();

        out.println("Escolha o modo de jogo: 1 para Jogador vs CPU, 2 para Jogador vs Jogador");
        String mode = in.readLine();

        if ("1".equals(mode)) {
          playingAgainstCPU = true;
          playAgainstCPU();
        } else if ("2".equals(mode)) {
          out.println("Aguardando outro jogador...");
          synchronized (gameHandlers) {
            for (GameHandler handler : gameHandlers) {
              if (handler != this && handler.opponent == null) {
                opponent = handler;
                handler.opponent = this;
                break;
              }
            }
          }
          if (opponent != null) {
            out.println("Jogador encontrado: " + opponent.name);
            opponent.out.println("Jogador encontrado: " + name);
            playAgainstPlayer();
          } else {
            out.println("Nenhum jogador disponível. Tente novamente mais tarde.");
          }
        } else {
          out.println("Modo inválido.");
        }
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          socket.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    private void playAgainstCPU() throws IOException {
      Random rand = new Random();
      String[] options = { "Pedra", "Papel", "Tesoura" };
      int wins = 0, losses = 0, draws = 0;

      while (true) {
        out.println("Escolha: Pedra, Papel ou Tesoura (ou Sair para terminar):");
        String userChoice = in.readLine();

        if ("Sair".equalsIgnoreCase(userChoice)) {
          out.println("Vitórias: " + wins + ", Derrotas: " + losses + ", Empates: " + draws);
          break;
        }

        String cpuChoice = options[rand.nextInt(3)];
        out.println("CPU escolheu: " + cpuChoice);
        String result = determineWinner(userChoice, cpuChoice);

        switch (result) {
          case "Vitória":
            wins++;
            out.println("Você ganhou!");
            break;
          case "Derrota":
            losses++;
            out.println("Você perdeu!");
            break;
          case "Empate":
            draws++;
            out.println("Empate!");
            break;
          default:
            out.println("Escolha inválida. Tente novamente.");
        }
      }
    }

    private void playAgainstPlayer() throws IOException {
      int wins = 0, losses = 0, draws = 0;
      int opponentWins = 0, opponentLosses = 0, opponentDraws = 0;

      while (true) {
        out.println("Escolha: Pedra, Papel ou Tesoura (ou Sair para terminar):");
        String userChoice = in.readLine();

        if ("Sair".equalsIgnoreCase(userChoice)) {
          out.println("Vitórias: " + wins + ", Derrotas: " + losses + ", Empates: " + draws);
          opponent.out
              .println("Vitórias: " + opponentWins + ", Derrotas: " + opponentLosses + ", Empates: " + opponentDraws);
          break;
        }

        opponent.out.println("Aguardando escolha do outro jogador...");
        String opponentChoice = opponent.in.readLine();

        if ("Sair".equalsIgnoreCase(opponentChoice)) {
          out.println("O oponente saiu do jogo.");
          break;
        }

        String result = determineWinner(userChoice, opponentChoice);

        switch (result) {
          case "Vitória":
            wins++;
            opponentLosses++;
            out.println("Você ganhou!");
            opponent.out.println("Você perdeu!");
            break;
          case "Derrota":
            losses++;
            opponentWins++;
            out.println("Você perdeu!");
            opponent.out.println("Você ganhou!");
            break;
          case "Empate":
            draws++;
            opponentDraws++;
            out.println("Empate!");
            opponent.out.println("Empate!");
            break;
          default:
            out.println("Escolha inválida. Tente novamente.");
            opponent.out.println("Escolha inválida. Tente novamente.");
        }
      }
    }

    private String determineWinner(String choice1, String choice2) {
      if (choice1.equalsIgnoreCase(choice2)) {
        return "Empate";
      }

      switch (choice1.toLowerCase()) {
        case "pedra":
          return (choice2.equalsIgnoreCase("Tesoura")) ? "Vitória" : "Derrota";
        case "papel":
          return (choice2.equalsIgnoreCase("Pedra")) ? "Vitória" : "Derrota";
        case "tesoura":
          return (choice2.equalsIgnoreCase("Papel")) ? "Vitória" : "Derrota";
        default:
          return "Escolha Inválida";
      }
    }
  }
}
