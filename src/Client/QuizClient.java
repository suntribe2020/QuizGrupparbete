package Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Katri Vidén
 * Date: 2020-11-17
 * Time: 10:13
 * Project: QuizGrupparbete
 * Copyright: MIT
 */
public class QuizClient extends JFrame implements ActionListener {

    int portNr = 54448;
    String host = "127.0.0.1";

    QuizClient() {

        try (
                Socket socketToServer = new Socket(host, portNr);
                PrintWriter writerOut = new PrintWriter(socketToServer.getOutputStream(), true);
                BufferedReader serverIn = new BufferedReader(new InputStreamReader(socketToServer.getInputStream()));
        ) {

            BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));

            String fromServer;
            String fromUser;

            while ((fromServer = serverIn.readLine()) != null) {
                System.out.println(fromServer);
                fromUser = userIn.readLine();
                if (fromUser != null) {
                    writerOut.println(fromUser);
                }
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public static void main(String[] args) {
        new QuizClient();
    }
}
