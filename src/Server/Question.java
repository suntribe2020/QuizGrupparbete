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
        addToAnswers();
        shuffleAnswers();
    }

    public void addToAnswers(){

        answers.add(answer);
        answers.add(wrongAnswer1);
        answers.add(wrongAnswer2);
        answers.add(wrongAnswer3);
    }

    public String getAnswer() {
        return answer;
    }

    public void shuffleAnswers(){
        Collections.shuffle(answers);
    }

    public String printQuestion(){
        String output = question + "\n";
        for (int i = 0; i < answers.size(); i++) {
            if (i == answers.size() - 1) {
                output += answers.get(i);
            } else {
                output += answers.get(i) + "\n";
            }
        }
        return output;
    }
}
