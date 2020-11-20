package Server;

import Server.MultiUserServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Katri Vidén
 * Date: 2020-11-17
 * Time: 10:14
 * Project: QuizGrupparbete
 * Copyright: MIT
 */
public class QuizServerListener {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(54448);
        System.out.println("Quizkampen server is running");
        try {
            while (true) {
                ServerSideGame game = new ServerSideGame();
                MultiUserServer playerX
                        = new MultiUserServer(serverSocket.accept(), 'X', game);
                System.out.println("Player X connected");
                MultiUserServer playerY
                        = new MultiUserServer(serverSocket.accept(), 'Y', game);
                System.out.println("Player Y connected");
                playerX.setOpponent(playerY);
                playerY.setOpponent(playerX);
                game.currentPlayer = playerX;
                playerX.start();
                playerY.start();

                /*
                final Socket socketToClient = serverSocket.accept();
                MultiUserServer multiUserServer =
                        new MultiUserServer(socketToClient);
                multiUserServer.start();

                 */
            }
        } finally {
            serverSocket.close();
        }
    }
}
