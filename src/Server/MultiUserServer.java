package Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Katri Vidén
 * Date: 2020-11-17
 * Time: 10:14
 * Project: QuizGrupparbete
 * Copyright: MIT
 */
public class MultiUserServer extends Thread {

    private final Socket clientSocket;

    public MultiUserServer(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run(){

        try (
                PrintWriter out =
                        new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
        ) {
            String inputLine;

            QuizProtocol protocol = new QuizProtocol();
            out.println(protocol.getInput(null));

            while ((inputLine = in.readLine()) != null) {
                out.println(protocol.getInput(inputLine));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
