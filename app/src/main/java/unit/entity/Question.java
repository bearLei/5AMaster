package unit.entity;

import java.util.List;

/**
 * Created by lei on 2018/6/29.
 */
public class Question {
    private String QuestionUID;
    private String Question;
    private String Answer;
    private List<Choice> Choices;

    public Question() {
    }

    public String getQuestionUID() {
        return QuestionUID;
    }

    public void setQuestionUID(String questionUID) {
        QuestionUID = questionUID;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public List<Choice> getChoices() {
        return Choices;
    }

    public void setChoices(List<Choice> choices) {
        Choices = choices;
    }
}
