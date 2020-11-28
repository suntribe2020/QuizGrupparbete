package Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game extends Thread {

    private final int numberOfRounds = Database.getNumberOfRounds();
    private final int numberOfQuestions = Database.getNumberOfQuestions();
    private Player firstPlayer;
    private Player secondPlayer;
    private int playedRounds = 0;

    public List<Question> currentCategory = new ArrayList<>();

    public Game(Player firstPlayer, Player secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }

    @Override
    public void run() {
        while (playedRounds <= numberOfRounds) {
            try {
                playRound();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void playRound() throws IOException {
        boolean isValidChoice = false;
        String chosenCategory = null;

        while (!isValidChoice) {
            chosenCategory = initiateRound(firstPlayer, "Please choose a category");

            if (chosenCategory.equals(Database.GameCategory.MUSIC.toString())) {
                currentCategory = Database.getMusicQuestions();
            } else if (chosenCategory.equals(Database.GameCategory.FILM.toString())) {
                currentCategory = Database.getFilmQuestions();
            } else if (chosenCategory.equalsIgnoreCase(Database.GameCategory.GAMES.toString())) {
                currentCategory = Database.getGameQuestions();
            } else if (chosenCategory.equalsIgnoreCase(Database.GameCategory.SPORT.toString())) {
                currentCategory = Database.getSportQuestions();
            }
            isValidChoice = true;
        }
        Collections.shuffle(currentCategory);

        // first player round
        playQuestionRound(firstPlayer);
        firstPlayer.writeToClient("Score this round: " + firstPlayer.getRoundScore() + ". Please wait for the opponent to " +
                "play round");

        // second player round
        String welcomeMessage = "Chosen category to play was: " + chosenCategory + ". Are you ready to start?";
        initiateRound(secondPlayer, welcomeMessage);
        playQuestionRound(secondPlayer);

        // end of both round checks and proceeding
        playedRounds++;
        firstPlayer.writeToClient("Score this round: " + firstPlayer.getRoundScore() + ". Opponent scored: " + secondPlayer.getRoundScore());
        writeScoreToSecondPlayerAndAwaitGo();

        if (checkIfGameOver()) { // is game is over, else flip players and go again
            writeEndMessage(firstPlayer, secondPlayer);
            writeEndMessage(secondPlayer, firstPlayer);
        }
        flipPlayers();
    }

    private void flipPlayers() {
        Player tempPlayer = secondPlayer;
        secondPlayer = firstPlayer;
        firstPlayer = tempPlayer;
    }

    private String initiateRound(Player player, String message) throws IOException {
        player.writeToClient(message);
        return player.readFromClient();
    }

    private void playQuestionRound(Player player) throws IOException {
        player.setRoundScore(0);
        for (int i = 0; i < numberOfQuestions; i++) {
            player.writeToClient(currentCategory.get(i).printQuestion());
            String answer = player.readFromClient();
            if (answer.equalsIgnoreCase(currentCategory.get(i).getAnswer())) {
                player.setRoundScore(player.getRoundScore() + 1);
            }
        }
        player.addToTotalScore(player.getRoundScore());
    }

    public void writeEndMessage(Player player1, Player player2) {
        player1.writeToClient("The game has ended. Your score was " + player1.getTotalScore() + ". Your opponent scored: " + player2.getTotalScore());
    }

    public void writeScoreToSecondPlayerAndAwaitGo() throws IOException {
        secondPlayer.writeToClient("You scored: " + secondPlayer.getRoundScore() + " this round. The opponent scored: "
                + firstPlayer.getRoundScore());
        if (playedRounds != Database.getNumberOfRounds()) {
            secondPlayer.readFromClient();
        }
    }

    public boolean checkIfGameOver() {
        return playedRounds == numberOfRounds;
    }
}