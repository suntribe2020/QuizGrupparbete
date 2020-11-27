package Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game extends Thread {

    private final int numberOfRounds = Database.getNumberOfRounds();
    private final int numberOfQuestions = Database.getNumberOfQuestions();
    private Player activePlayer;
    private Player waitingPlayer;
    private boolean isGameOver;
    private int playedRounds = 0;
    private String answer;
    private int tempScore;

    public List<Question> currentCategory = new ArrayList<>(4);

    public Game(Player activePlayer, Player waitingPlayer) {
        this.activePlayer = activePlayer;
        this.waitingPlayer = waitingPlayer;
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
                result = initiateRound(activePlayer, "Please choose a category");
            } else{
                result = initiateRound(activePlayer, "Your score this round was: " + tempScore + " Please choose a category");
            }

            if (result.equals(Database.GameCategory.MUSIC.toString())) {
                currentCategory = Database.getMusicQuestions();
            } else if (result.equals(Database.GameCategory.FILM.toString())) {
                currentCategory = Database.getFilmQuestions();
            } else if (result.equalsIgnoreCase(Database.GameCategory.GAMES.toString())) {
                currentCategory = Database.getGameQuestions();
            } else if (result.equalsIgnoreCase(Database.GameCategory.SPORT.toString())) {
                currentCategory = Database.getSportQuestions();
            }
            isValidChoice = true;
        }
        Collections.shuffle(currentCategory);

        playQuestionRound(activePlayer);

        //player two round
        String welcomeMessage = "Chosen categories to play was: " + result + " Are you ready to start?";
        initiateRound(waitingPlayer, welcomeMessage);
        playQuestionRound(waitingPlayer);
        flipPlayers();
    }

    private void flipPlayers() {
        Player tempPlayer = waitingPlayer;
        waitingPlayer = activePlayer;
        activePlayer = tempPlayer;
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

        if(player.equals(waitingPlayer)){
            playedRounds++;
            if(playedRounds==1) {
                player.writeToClient("Score this round: " + player.getRoundScore() + ". Oppenent scored: " + activePlayer.getRoundScore());
                System.out.println("Score this round: " + player.getRoundScore() + ". Oppenent scored: " + waitingPlayer.getRoundScore());
//                playerToStart.writeToClient("Your opponent scored:  " + player.getRoundScore());
            } else {
                player.writeToClient("Score this round: " + player.getRoundScore() + ". Total score:  " + player.getTotalScore());
                System.out.println("Två");
//                playerToStart.writeToClient("Your opponent scored:  " + player.getRoundScore() + ". Their total score:  " + player.getTotalScore());
            }

            tempScore = waitingPlayer.getRoundScore();
            activePlayer.setRoundScore(0);
            waitingPlayer.setRoundScore(0);
        } else {
            if(playedRounds==0) {
                player.writeToClient("Score this round: " + player.getRoundScore() + ". Waiting for opponent to finish round");
            } else player.writeToClient("Score this round: " + player.getRoundScore() + ". Total score:  " + player.getTotalScore()+ ". Waiting for opponent to finish round");
        }
        isGameOver = checkIfGameOver();
        if(isGameOver) {
            writeEndMessage(activePlayer, waitingPlayer);
            writeEndMessage(waitingPlayer, activePlayer);
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