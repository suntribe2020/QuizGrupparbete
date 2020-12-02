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

        String chosenCategory = initiateRound(firstPlayer, ServerInstruction.FIRST_PLAYER_ROUND_START,
                "Please choose a category");
        Database.GameCategory category = Database.GameCategory.valueOf(chosenCategory.toUpperCase());
        currentCategory = Database.getQuestions(category);
        Collections.shuffle(currentCategory);

        // first player round
        playQuestionRound(firstPlayer);
        sendFirstPlayerScore(true);

        // second player round
        String secondPlayerReadyMessage = "Chosen category to play was: " + chosenCategory + ". Are you ready to start?";
        initiateRound(secondPlayer, ServerInstruction.SECOND_PLAYER_ROUND_START, secondPlayerReadyMessage);
        playQuestionRound(secondPlayer);

        // end of both round checks and proceeding
        playedRounds++;
        sendFirstPlayerScore(false);
        sendSecondPlayerScore();

        if (checkIfGameOver()) { // is game over, else flip players and go again
            writeEndMessage(firstPlayer, secondPlayer);
            writeEndMessage(secondPlayer, firstPlayer);
        }
        flipPlayers();
    }

    private String initiateRound(Player player, ServerInstruction instruction, String message) throws IOException {
        player.writeToClient(instruction.name());
        player.writeToClient(message);
        return player.readFromClient();
    }

    private void playQuestionRound(Player player) throws IOException {
        player.setRoundScore(0);
        for (int i = 0; i < numberOfQuestions; i++) {
            player.writeToClient(ServerInstruction.QUESTION.name());
            player.writeToClient(currentCategory.get(i).printQuestion());
            String answer = player.readFromClient();
            if (answer.equalsIgnoreCase(currentCategory.get(i).getAnswer())) {
                player.setRoundScore(player.getRoundScore() + 1);
                player.writeToClient(ServerInstruction.CORRECT_ANSWER.name());
                player.writeToClient(answer);
                player.readFromClient();
            }
            else if (!answer.equalsIgnoreCase(currentCategory.get(i).getAnswer())) {
                player.writeToClient(ServerInstruction.INCORRECT_ANSWER.name());
                player.writeToClient(answer);
                player.readFromClient();
            }
        }
        player.addToTotalScore(player.getRoundScore());
    }

    private void sendFirstPlayerScore(boolean firstReport) {
        firstPlayer.writeToClient(ServerInstruction.FIRST_PLAYER_SCORE.name());
        if (firstReport) {
            // first score report (without opponent score)
            firstPlayer.writeToClient("Score this round: " + firstPlayer.getRoundScore() + ". Please wait for the opponent " +
                    "to play round");
        } else {
            // second score report (with opponent score)
            firstPlayer.writeToClient(getRoundAndTotalScoreMessage(firstPlayer, secondPlayer));
        }
    }

    public void sendSecondPlayerScore() throws IOException {
        secondPlayer.writeToClient(ServerInstruction.SECOND_PLAYER_SCORE.name());
        secondPlayer.writeToClient(getRoundAndTotalScoreMessage(secondPlayer, firstPlayer));

        secondPlayer.readFromClient();
    }

    private String getRoundAndTotalScoreMessage(Player activePlayer, Player passivePlayer) {
        return "Round score: You: " + activePlayer.getRoundScore() + ". Opponent: "
                + passivePlayer.getRoundScore() + ". Total score: You: " + activePlayer.getTotalScore() + ". Opponent: " + passivePlayer.getTotalScore();
    }

    public boolean checkIfGameOver() {
        return playedRounds == numberOfRounds;
    }

    public void writeEndMessage(Player player1, Player player2) {
        player1.writeToClient(ServerInstruction.GAME_ENDED.name());
        player1.writeToClient("The game has ended. Your score was " + player1.getTotalScore() + ". Your opponent scored: "
                + player2.getTotalScore());
    }

    private void flipPlayers() {
        Player tempPlayer = secondPlayer;
        secondPlayer = firstPlayer;
        firstPlayer = tempPlayer;
    }
}