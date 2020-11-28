package Server;

import java.io.IOException;
import java.net.ServerSocket;

public class QuizServerListener {
    Player playerToStart;
    Player playerToWait;

    public QuizServerListener() throws IOException {
        Database.populate();
        try (ServerSocket serverSocket = new ServerSocket(54448)) {
            System.out.println("Quizkampen server is running");
            while (true) {
                playerToStart = new Player(serverSocket.accept(), '1');
                playerToStart.writeToClient("Welcome Player " + playerToStart.playerSignature + ". Waiting for another " +
                        "player");
                System.out.println("Player 1 connected");
                playerToWait = new Player(serverSocket.accept(), '2');
                System.out.println("Player 2 connected");
                playerToWait.writeToClient("Welcome Player " + playerToWait.playerSignature + ". Waiting for " +
                        "Player 1 to choose a category and answer the questions");

                Game game = new Game(playerToStart, playerToWait);
                game.start();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        QuizServerListener q = new QuizServerListener();
    }
}