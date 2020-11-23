package Server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Katri Vidén
 * Date: 2020-11-17
 * Time: 10:16
 * Project: QuizGrupparbete
 * Copyright: MIT
 */
public class QuestionDatabase {

    private static List<Question> musicQuestions = new ArrayList<>();
    private static List<Question> filmQuestions = new ArrayList<>();
    private static List<Question> gameQuestions = new ArrayList<>();
    private static List<Question> sportQuestions = new ArrayList<>();


    public QuestionDatabase() {

        createMusicQuestions();
        createFilmQuestions();
        createGameQuestions();
        createSportQuestions();

        Collections.shuffle(musicQuestions);
        Collections.shuffle(filmQuestions);
        Collections.shuffle(gameQuestions);
        Collections.shuffle(sportQuestions);

    }

    private void createSportQuestions() {
        sportQuestions.add(new Question("Which team has won the most Premier League titles?",
                "Manchester United",
                "Liverpool FC",
                "Arsenal FC",
                "Everton FC"));
        sportQuestions.add(new Question("Which sport does Los Angeles Lakers and New York Knicks play?",
                "Basketball",
                "Football",
                "Baseball",
                "Softball"));
        sportQuestions.add(new Question("In what country was the Olympic Winter Games held in 2010?",
                "Canada",
                "USA",
                "Germany",
                "Austria"));
        sportQuestions.add(new Question("What country won the Ice Hockey World Championship in 1987?",
                "Sweden",
                "Finland",
                "Russia",
                "USA"));
    }

    private void createGameQuestions() {
        gameQuestions.add(new Question("How many pieces of triforce does Link have to collect in 'The Legend of Zelda'?",
                "3",
                "2",
                "5",
                "6"));
        gameQuestions.add(new Question("In which year was Donkey Kong released for arcade?",
                "1981",
                "1979",
                "1984",
                "1986"));
        gameQuestions.add(new Question("In the game 'Cuphead', what is the color of Mugman?",
                "Blue",
                "Yellow",
                "Red",
                "Orange"));
        gameQuestions.add(new Question("What is the name of the first weapon you are given in 'Fortnite Battle Royal'?",
                "PickAxe",
                "KickBack",
                "TripSack",
                "PickSmash"));
    }

    private void createFilmQuestions() {
        filmQuestions.add(new Question("What is the name of the leading actor in the movie 'Interstellar'?",
                "Matthew McConaughey",
                "Brad Pitt",
                "Tom Cruise",
                "Johnny Depp"));
        filmQuestions.add(new Question("Fill in the blank: Cobra ___?",
                "Kai",
                "Key",
                "Kaj",
                "Cai"));
        filmQuestions.add(new Question("In which movie can you hear the famous line: 'I'm gonna make him an offer he can't refuse?",
                "The Godfather",
                "Goodfellas",
                "Scarface",
                "The Terminator"));
        filmQuestions.add(new Question("What is the name of the actor who plays Ace Ventura?",
                "Jim Carrey",
                "Adam Sandler",
                "Harrison Ford",
                "Kevin Hart"));
    }

    private void createMusicQuestions() {
        musicQuestions.add(new Question("Which band sings the song 'Smoke On The Water'?",
                "Deep Purple",
                "Beatles",
                "Aerosmith",
                "Black Sabbath"));
        musicQuestions.add(new Question("What is the name of the artist with the hit song 'Smooth Criminal'?",
                "Michael Jackson",
                "Cher",
                "Madonna",
                "Janet Jackson"));
        musicQuestions.add(new Question("When did Madonna release her first album?",
                "1983",
                "1979",
                "1980",
                "1987"));
        musicQuestions.add(new Question("What is Eminems real name?",
                "Marshall Mathers",
                "Slim Shady",
                "Marshall Matthews",
                "Matthew Mathers"));
    }

    public ArrayList<String> pickACategory() {
        ArrayList<String> categories = new ArrayList<>();

        categories.add("Music");
        categories.add("Film");
        categories.add("Games");
        categories.add("Sport");

        return categories;
    }

    public static List<Question> getMusicQuestions() {
        return musicQuestions;
    }

    public static List<Question> getFilmQuestions() {
        return filmQuestions;
    }

    public static List<Question> getGameQuestions() {
        return gameQuestions;
    }

    public static List<Question> getSportQuestions() {
        return sportQuestions;
    }

}
