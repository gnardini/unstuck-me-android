package ar.com.wolox.unstuckme.model;

import java.util.List;

public class Question {

    private String question;
    private String url;
    private List<Choise> choises;

    public String getQuestion() {
        return question;
    }

    public String getUrl() {
        return url;
    }

    public List<Choise> getChoises() {
        return choises;
    }
}
