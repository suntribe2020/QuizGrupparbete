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

    //-----------------------------------------------------------------------

    private String[] filmQuestions = {"What is the name of the leading actor in the movie 'Interstellar'?",
            "Fill in the blank: Cobra ___?",
            "In which movie can you hear the famous line: 'I'm gonna make him an offer he can't refuse?",
            "What is the name of the actor who plays Ace Ventura?"};

    private String[] filmAlternatives = {"Matthew McConaughey, Brad Pitt, Tom Cruise, Johnny Depp",
            "Key, Kaj, Cai, Kai",
            "Goodfellas, Scarface, The Godfather, The Terminator",
            "Adam Sandler, Harrison Ford, Kevin Hart, Jim Carrey"};

    private String[] filmRightAnswers = {"Matthew McConaughey", "Kai", "The Godfather", "Jim Carrey"};

    //----------------------------------------------------------------------

    private String[] gameQuestions = {"How many pieces of triforce does Link have to collect in 'The Legend of Zelda'?",
            "In which year was Donkey Kong released for arcade?",
            "In the game 'Cuphead', what is the color of Mugman?",
            "What is the name of the first weapon you are given in 'Fortnite Battle Royal'?"};

    private String[] gameAlternatives = {"2, 5, 3, 6",
            "1979, 1981, 1984, 1986",
            "Yellow, Blue, Red, Orange",
            "PickAxe, KickBack, TripSack, PickSmash"};

    private String[] gameRightAnswers = {"3", "1981", "Blue", "PickAxe"};

    //-----------------------------------------------------------------------

    private String[] sportQuestions = {"Which team has won the most Premier League titles?",
            "Which sport does Los Angeles Lakers and New York Knicks play?",
            "In what country was the Olympic Winter Games held in 2010?",
            "What country won the Ice Hockey World Championship in 1987?"};

    private String[] sportAlternatives = {"Liverpool FC, Arsenal FC, Manchester United, Everton FC",
            "Basketball, Football, Baseball, Softball",
            "USA, Germany, Austria, Canada",
            "Finland, Russia, Sweden, USA"};

    private String[] sportRightAnswers = {"Manchester United", "Basketball", "Canada", "Sweden"};

    public QuestionDatabase() {
    }

    public ArrayList<String> pickACategory() {
        ArrayList<String> categories = new ArrayList<>();

        categories.add("Music");
        categories.add("Film");
        categories.add("Games");
        categories.add("Sport");

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

    public String[] getFilmQuestions() {
        return filmQuestions;
    }

    public String[] getFilmAlternatives() {
        return filmAlternatives;
    }

    public String[] getFilmRightAnswers() {
        return filmRightAnswers;
    }

    public String[] getGameQuestions() {
        return gameQuestions;
    }

    public String[] getGameAlternatives() {
        return gameAlternatives;
    }

    public String[] getGameRightAnswers() {
        return gameRightAnswers;
    }

    public String[] getSportQuestions() {
        return sportQuestions;
    }

    public String[] getSportAlternatives() {
        return sportAlternatives;
    }

    public String[] getSportRightAnswers() {
        return sportRightAnswers;
    }
}
