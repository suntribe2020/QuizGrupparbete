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
        while (true) {
            try {
                final Socket socketToClient = serverSocket.accept();
                MultiUserServer multiUserServer =
                        new MultiUserServer(socketToClient);
                multiUserServer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}