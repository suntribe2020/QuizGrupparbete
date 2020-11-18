package Server;

import java.util.ArrayList;

/**
 * Created by Katri Vidén
 * Date: 2020-11-17
 * Time: 10:16
 * Project: QuizGrupparbete
 * Copyright: MIT
 */
public class QuestionDatabase {

    private String[] musicQuestions = {"Which band sings the song 'Smoke On The Water'?",
            "What is the name of the artist with the hit song 'Smooth Criminal'?",
            "When did Madonna release her first album?",
            "What is Eminems real name?"};

    private String[] musicAlternatives = {"Beatles, Deep Purple, Aerosmith, Black Sabbath",
            "Cher, Madonna, Michael Jackson, Janet Jackson",
            "1979, 1980, 1983, 1987",
            "Slim Shady, Marshall Matthews, Matthew Mathers, Marshall Mathers"};

    private String[] musicRightAnswers = {"Deep Purple", "Michael Jackson", "1983", "Marshall Mathers"};

    //comment---------------------------------------------------------------------

    private String[] filmQuestions = {"What is the name of the leading actor in the movie 'Interstellar'?",
            "Fill in the blank: Cobra ___?"};

    private String[] filmAlternatives = {"Matthew McConaughey, Brad Pitt, Tom Cruise, Johnny Depp",
            "Key, Kaj, Cai, Kai"};

    private String[] filmRightAnswers = {"Matthew McConaughey", "Kai"};

    public QuestionDatabase() {
    }

    public ArrayList<String> pickACategory() {
        ArrayList<String> categories = new ArrayList<>();

        categories.add("Music");
        categories.add("Film");
        categories.add("Games");

        return categories;
    }

    public String[] getMusicQuestions() {
        return musicQuestions;
    }

    public String[] getMusicAlternatives() {
        return musicAlternatives;
    }

    public String[] getMusicRightAnswers() {
        return musicRightAnswers;
    }
}
