package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Player {

    private char playerSignature;
    private Socket socket;
    private BufferedReader socketInput;
    private PrintWriter socketOutput;
    private int roundScore=0;
    private int totalScore=0;

    public Player(Socket socket, char playerSignature) {
        this.socket = socket;
        this.playerSignature = playerSignature;
        setUpSocketCommunication();
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

    public char getPlayerSignature() {
        return playerSignature;
    }
}