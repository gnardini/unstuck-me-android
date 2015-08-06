package ar.com.wolox.unstuckme.model;

public class User {

    private int id;
    private int answeredQuestions;
    private int myQuestionsAnswers;
    private int questionsAsked;
    private int credits;
    private int level;
    private int currentExp;
    private int expToLevelUp;

    public User(int id,
                int answeredQuestions,
                int myQuestionsAnswers,
                int questionsAsked,
                int credits,
                int level,
                int currentExp,
                int expToLevelUp) {
        this.id = id;
        this.answeredQuestions = answeredQuestions;
        this.myQuestionsAnswers = myQuestionsAnswers;
        this.questionsAsked = questionsAsked;
        this.credits = credits;
        this.level = level;
        this.currentExp = currentExp;
        this.expToLevelUp = expToLevelUp;
    }

    public int getId() {
        return id;
    }

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

    public void addCredits(int credits) {
        this.credits += credits;
    }
}
