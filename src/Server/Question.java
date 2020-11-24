package Server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getWrongAnswer1() {
        return wrongAnswer1;
    }

    public String getWrongAnswer2() {
        return wrongAnswer2;
    }

    public String getWrongAnswer3() {
        return wrongAnswer3;
    }
}
