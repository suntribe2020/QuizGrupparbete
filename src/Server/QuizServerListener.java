package Server;

import java.io.IOException;
import java.net.ServerSocket;

public class QuizServerListener {
    static Player playerToStart;
    static Player playerToWait;

    public static void main(String[] args) throws IOException {
        Database.populate();
        try (ServerSocket serverSocket = new ServerSocket(54448)) {
            System.out.println("Quizkampen server is running");
            while (true) {
                playerToStart = new Player(serverSocket.accept(), '1');
                playerToStart.writeToClient("Welcome player" + playerToStart.playerSignature + " You are now waiting for another " +
                        "player");
                System.out.println("Player 1 connected");
                playerToWait = new Player(serverSocket.accept(), '2');
                System.out.println("Player 2 connected");
                playerToWait.writeToClient("Welcome player" + playerToWait.playerSignature + " You are now waiting for the first " +
                        "player to choose a category and answer the questions");

                Game game = new Game(playerToStart, playerToWait);
                game.start();
            }
        }
    }
}