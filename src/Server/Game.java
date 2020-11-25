package Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game extends Thread {

    private final int numberOfRounds = Database.getNumberOfRounds();
    private final int numberOfQuestions = Database.getNumberOfQuestions();
    private Player playerToStart;
    private Player playerToWait;

    public Game(Player playerToStart, Player playerToWait) {
        this.playerToStart = playerToStart;
        this.playerToWait = playerToWait;
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
        String currentCategory = getCategoryFromPlayer(playerToStart);
        //List<String> answers1 = new ArrayList<String>;
        //List<String> answers2 = new ArrayList<String>[];
        for (int i = 0; i < numberOfQuestions; i++) {
            //answers1.add(askPlayerQuestion(playerToStart, currentCategory, i));
        }
        for (int i = 0; i < numberOfQuestions; i++) {
            //answers1.add(askPlayerQuestion(playerToStart, currentCategory, i));
        }


        /*String result = initiateRound(playerToStart, "Please choose a category: Music, Film, Games, Sport");

        playQuestionRound(playerToStart);

        //player two round
        String welcomeMessage = "Chosen categories to play was: " + result + " Are you ready to start?";
        initiateRound(playerToWait, welcomeMessage);
        playQuestionRound(playerToWait);
        flipPlayers();*/
    }

    private void flipPlayers() {
        Player tempPlayer = playerToWait;
        playerToWait = playerToStart;
        playerToStart = tempPlayer;
    }

    private String initiateRound(Player player, String message) throws IOException {
        //player.writeToClient(message);

        String answer = player.readFromClient();
        //System.out.println("Answer from player 2 is:   " + answer);
        return (answer);
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

    private String getCategoryFromPlayer(Player player) throws IOException {
        player.writeToClient("CAT");
        String categoryString = player.readFromClient();
        return (categoryString);
    }

    private String askPlayerQuestion(Player player, String category, int questionNumber) throws IOException {
        player.writeToClient("QUE");
        player.writeToClient(category);
        player.writeToClient(String.valueOf(questionNumber));
        return(player.readFromClient());
    }
}
