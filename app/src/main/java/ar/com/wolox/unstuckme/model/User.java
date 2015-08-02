package ar.com.wolox.unstuckme.model;

public class User {

    private String id;
    private int answeredQuestions;
    private int myQuestionsAnswers;
    private int questionsAsked;
    private int credits;
    private int level;
    private int currentExp;
    private int expToLevelUp;

    public int getAnsweredQuestions() {
        return answeredQuestions;
    }

    public int getMyQuestionsAnswers() {
        return myQuestionsAnswers;
    }

    public int getQuestionsAsked() {
        return questionsAsked;
    }

    public int getCredits() {
        return credits;
    }

    public int getLevel() {
        return level;
    }

    public int getCurrentExp() {
        return currentExp;
    }

    public int getExpToLevelUp() {
        return expToLevelUp;
    }
}
