package Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game extends Thread {

    private final int numberOfRounds = Database.getNumberOfRounds();
    private final int numberOfQuestions = Database.getNumberOfQuestions();
    private Player playerToStart;
    private Player playerToWait;
    private boolean isGameOver;
    private int playedRounds = 0;

    public List<Question> currentCategory = new ArrayList<>(4);

    public Game(Player playerToStart, Player playerToWait) {
        this.playerToStart = playerToStart;
        this.playerToWait = playerToWait;
    }

    @Override
    public void run() {
        while(playedRounds<=numberOfRounds) {
            try {
                playRound();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /*private String getWelcomeMessage(Player currentPlayer){
        String firstMessage = "Please choose a category: Music, Film, Games, Sport";
        if (playedRounds !=0) {
           firstMessage = "Your current score is " + currentPlayer.getTotalScore()
        }
    }*/

    private void playRound() throws IOException {
        boolean isValidChoice = false;
        String result = null;

        while (!isValidChoice) {
            if (playedRounds==0) {
                result = initiateRound(playerToStart, "Please choose a category");
            } else{
                result = initiateRound(playerToStart, "Your score this round was: " + playerToStart.getRoundScore() + " Please choose a category");
            }
            if (result.equalsIgnoreCase("Music")) {

                currentCategory = Database.getMusicQuestions();
                isValidChoice = true;
            } else if (result.equalsIgnoreCase("Film")) {
                currentCategory = Database.getFilmQuestions();
                isValidChoice = true;
            } else if (result.equalsIgnoreCase("Games")) {

                currentCategory = Database.getGameQuestions();
                isValidChoice = true;
            } else if (result.equalsIgnoreCase("Sport")) {

                currentCategory = Database.getSportQuestions();
                isValidChoice = true;
            } else {
                initiateRound(playerToStart, "Choose a valid option");
            }
        }
        Collections.shuffle(currentCategory);

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

        for(Question q : currentCategory) {
            System.out.println("Test");
            System.out.println(q.printQuestion());
            System.out.println("Test over");
        }

        for(int i = 0; i<numberOfQuestions; i++) {
            player.writeToClient(currentCategory.get(i).printQuestion());
            String answer = player.readFromClient();
            if (answer.equalsIgnoreCase(currentCategory.get(i).getAnswer())){
                player.setRoundScore(player.getRoundScore()+1);
            }
        }

        player.addToTotalScore(player.getRoundScore());

        if(player.equals(playerToWait)){
            playedRounds++;
            if(playedRounds==1) {
                player.writeToClient("Score this round: " + player.getRoundScore() + ". Oppenent scored: " + playerToStart.getRoundScore());
//                playerToStart.writeToClient("Your opponent scored:  " + player.getRoundScore());
            } else {
                player.writeToClient("Score this round: " + player.getRoundScore() + ". Total score:  " + player.getTotalScore());
//                playerToStart.writeToClient("Your opponent scored:  " + player.getRoundScore() + ". Their total score:  " + player.getTotalScore());
            }
            playerToStart.setRoundScore(0);
            playerToWait.setRoundScore(0);
        } else {
            if(playedRounds==0) {
                player.writeToClient("Score this round: " + player.getRoundScore() + ". Waiting for opponent to finish round");
            } else player.writeToClient("Score this round: " + player.getRoundScore() + ". Total score:  " + player.getTotalScore()+ ". Waiting for opponent to finish round");
        }
        isGameOver = checkIfGameOver();
        if(isGameOver) {
            writeEndMessage(playerToStart, playerToWait);
            writeEndMessage(playerToWait, playerToStart);
        }
    }

    public void writeEndMessage(Player player1, Player player2) {
        player1.writeToClient("The game has ended. Your score was " + player1.getTotalScore() + ". Your opponent scored: " + player2.getTotalScore());
    }

    public boolean checkIfGameOver(){
        return playedRounds == numberOfRounds;
    }

    public int getPlayedRounds() {
        return playedRounds;
    }
}