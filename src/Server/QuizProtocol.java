package Server;

import java.util.ArrayList;

/**
 * Created by Katri Vidén
 * Date: 2020-11-17
 * Time: 10:16
 * Project: QuizGrupparbete
 * Copyright: MIT
 */
public class QuizProtocol {

    private static final int WAITING = 0;
    private static final int SENT_QUESTION = 1;
    private static final int SENT_ANSWER = 2;

    private int state = WAITING;
    private int counter = 0;

    QuestionDatabase questionDatabase = new QuestionDatabase();

    public String getInput(String input) {
        String theOutput = null;

        ArrayList<String> category = questionDatabase.pickACategory();

        while (true) {
            if (state == WAITING || state == SENT_ANSWER) {
                theOutput = questionDatabase.getMusicQuestions()[counter] + " Alternatives are: " +
                        questionDatabase.getMusicAlternatives()[counter];
                state = SENT_QUESTION;
            } else if (state == SENT_QUESTION) {
                if (input.equalsIgnoreCase(questionDatabase.getMusicRightAnswers()[counter])) {
                    theOutput = "You answered correctly!";
                    state = SENT_QUESTION;
                } else {
                    theOutput = "You answered wrong!";
                    state = SENT_QUESTION;
                }
                state = SENT_ANSWER;
                counter++;
            }
            return theOutput;
        }
    }
}
