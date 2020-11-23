package Server;

import java.io.IOException;
import java.util.List;

public class Game extends Thread {
    //Change to property file
    private final static int NUMBER_OF_ROUNDS = 3;
    private final static int NUMBER_OF_QUESTIONS = 4;
    private Player playerToStart;
    private Player playerToWait;

    public List<Question> currentCategory;

    public Game(Player playerToStart, Player playerToWait) {
        this.playerToStart = playerToStart;
        this.playerToWait = playerToWait;
    }

    @Override
    public void run() {
        //for (int i = 0; i < NUMBER_OF_ROUNDS; i++) {
        while (true) {
            try {
                playRound();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void playRound() throws IOException {
        boolean isValidChoice = false;
        String result = null;

        while (!isValidChoice) {
            result = initiateRound(playerToStart, "Please choose a category: Music, Film, Games, Sport");
            if (result.equalsIgnoreCase("Music")) {
                currentCategory = QuestionDatabase.getMusicQuestions();
                isValidChoice = true;
            } else if (result.equalsIgnoreCase("Film")) {
                currentCategory = QuestionDatabase.getFilmQuestions();
                isValidChoice = true;
            } else if (result.equalsIgnoreCase("Games")) {
                currentCategory = QuestionDatabase.getGameQuestions();
                isValidChoice = true;
            } else if (result.equalsIgnoreCase("Sport")) {
                currentCategory = QuestionDatabase.getSportQuestions();
                isValidChoice = true;
            } else {
                initiateRound(playerToStart, "Choose a valid option");
                isValidChoice = false;
            }


        }

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
}
