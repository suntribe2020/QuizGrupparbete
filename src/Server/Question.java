package Server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Julia Wigenstedt
 * Date: 2020-11-23
 * Time: 16:06
 * Project: GrupparbeteActual
 * Copyright: MIT
 */
public class Question {
    private String question;
    private String answer;
    private String wrongAnswer1;
    private String wrongAnswer2;
    private String wrongAnswer3;
    private List<String> answers;

    public Question(String question, String answer, String wrongAnswer1, String wrongAnswer2, String wrongAnswer3){
        this.question=question;
        this.answer=answer;
        this.wrongAnswer1=wrongAnswer1;
        this.wrongAnswer2=wrongAnswer2;
        this.wrongAnswer3=wrongAnswer3;

        answers = new ArrayList<>();
        addToAnswersAndShuffle();
    }

    public void addToAnswersAndShuffle(){
        answers.add(answer);
        answers.add(wrongAnswer1);
        answers.add(wrongAnswer2);
        answers.add(wrongAnswer3);
        Collections.shuffle(answers);
    }

    public List<String> getAnswers() {
        return answers;
    }
}
