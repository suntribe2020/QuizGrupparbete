package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Player extends Thread {

    char playerSignature;
    Player opponent;
    Socket socket;
    BufferedReader socketInput;
    PrintWriter socketOutput;
    QuestionDatabase questionDatabase = new QuestionDatabase();
    private int roundScore=0;
    private int totalScore=0;

    public Player(Socket socket, char playerSignature) {
        this.socket = socket;
        this.playerSignature = playerSignature;
        setUpSocketCommunication();
    }
    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public Player getOpponent() {
        return opponent;
    }

    public void otherPlayerMoved(int location) {
        socketOutput.println("OPPONENT_MOVED " + location);
    }


    @Override
    public void run() {
        try {
            while (true) {
                writeToClient("First message. Hello from player" + this.playerSignature);
                int questionIndex;
                String command = socketInput.readLine();
                if (true) {
                    System.out.println("ValidMOOOVE");
                    if (command.startsWith("Musik")) {
                        questionIndex = Integer.parseInt(command.substring(5));
                        socketOutput.println(questionDatabase.getMusicQuestions().get(questionIndex));
                    } else if (command.startsWith("Spel")) {
                        questionIndex = Integer.parseInt(command.substring(4));
                        socketOutput.println(questionDatabase.getGameQuestions().get(questionIndex));
                    } else if (command.startsWith("Film")) {
                        questionIndex = Integer.parseInt(command.substring(4));
                        socketOutput.println(questionDatabase.getFilmQuestions().get(questionIndex));
                    } else if (command.startsWith("Sport")) {
                        questionIndex = Integer.parseInt(command.substring(5));
                        socketOutput.println(questionDatabase.getSportQuestions().get(questionIndex));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }

    private void setUpSocketCommunication() {
        try {
            generateSocketReader();
            generateSocketWriter();
        } catch (IOException e) {
            System.out.println("Error when setting up communication links to server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void generateSocketReader() throws IOException {
        this.socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private void generateSocketWriter() throws IOException {
        socketOutput = new PrintWriter(socket.getOutputStream(), true);
    }

    public void writeToClient(String message) {
        this.socketOutput.println(message);
        socketOutput.flush();
    }

    public String readFromClient() throws IOException {
        return this.socketInput.readLine();
    }

    public int getRoundScore() {
        return roundScore;
    }

    public void setRoundScore(int score) {
        this.roundScore = score;
    }

    public void addToTotalScore(int roundScore) {
        totalScore += roundScore;
    }

    public int getTotalScore() {
        return totalScore;
    }
}