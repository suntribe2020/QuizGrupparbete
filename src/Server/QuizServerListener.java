package Server;

import java.io.IOException;
import java.net.ServerSocket;

public class QuizServerListener {
    private Player playerToStart;
    private Player playerToWait;

    public QuizServerListener() throws IOException {
        Database.populate();
        try (ServerSocket serverSocket = new ServerSocket(54448)) {
            System.out.println("Quizkampen server is running");
            while (true) {
                playerToStart = new Player(serverSocket.accept(), '1');
                playerToStart.writeToClient("Welcome player " + playerToStart.getPlayerSignature() + ". \\nYou are now waiting for another " +
                        "player");
                System.out.println("Player 1 connected");
                playerToWait = new Player(serverSocket.accept(), '2');
                System.out.println("Player 2 connected");
                playerToWait.writeToClient("Welcome player " + playerToWait.getPlayerSignature() + ". \\nYou are now waiting for the first " +
                        "player to choose a category and answer the questions");

                Game game = new Game(playerToStart, playerToWait);
                game.start();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        QuizServerListener q = new QuizServerListener();
    }
}