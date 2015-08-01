package ar.com.wolox.unstuckme.model;

public class Option {

    private int id;
    private String option;
    private int votes;
    private int percentage;

    public int getId() {
        return id;
    }

    public String getOption() {
        return option;
    }

    public int getVotes() {
        return votes;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}
