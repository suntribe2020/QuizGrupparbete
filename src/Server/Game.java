package Server;

import Server.properties.PropertiesHandler;

import java.io.IOException;
import java.util.Properties;

public class Game extends Thread {
    private int numberOfRounds = 0;
    private int numberOfQuestions = 0;
    private Player playerToStart;
    private Player playerToWait;

    public Game(Player playerToStart, Player playerToWait) {
        this.playerToStart = playerToStart;
        this.playerToWait = playerToWait;
        loadProperties();
    }

    @Override
    public void run() {
        for (int i = 0; i < numberOfRounds; i++) {
            try {
                playRound();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void playRound() throws IOException {
        String result = initiateRound(playerToStart, "Please choose a category: Music, Film, Games, Sport");
        playQuestionRound(playerToStart);


        //player two round
        String welcomeMessage = "Chosen categories to play was: " + result + " Are you ready to start?";
        initiateRound(playerToWait, welcomeMessage);
        playQuestionRound(playerToWait);
        flipPlayers();
    }

    private void flipPlayers() {
        Player tempPlayer = playerToWait;
        playerToWait = playerToStart;
        playerToStart = tempPlayer;
    }

    private String initiateRound(Player player, String message) throws IOException {
        player.writeToClient(message);
        String answer = player.readFromClient();
        //System.out.println("Answer from player 2 is:   " + answer);
        return answer;
    }

    private void playQuestionRound(Player player) throws IOException {
        player.writeToClient("Question 1: How are you today?");
        String firstAnswerPlayer = player.readFromClient();
        player.writeToClient("Question 2: Yesterday was what day?");
        String secondAnswerPlayer = player.readFromClient();
        player.writeToClient("Question 3: What is the uindi?");
        String thirdAnswerPlayer = player.readFromClient();
        player.writeToClient("Question 4: How are your family?");
        String fourthAnswerPlayer = player.readFromClient();
        player.writeToClient("Good job!");
    }

    private void loadProperties() {
        Properties properties = PropertiesHandler.getProperties();
        numberOfRounds = Integer.parseInt(properties.getProperty("NUMBER_OF_ROUNDS", "3"));
        numberOfQuestions = Integer.parseInt(properties.getProperty("NUMBER_OF_QUESTIONS", "4"));
    }
}
