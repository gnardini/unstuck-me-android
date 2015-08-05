package ar.com.wolox.unstuckme.model.event;

import ar.com.wolox.unstuckme.model.Question;

public class QuestionAnsweredEvent {

    private Question mQuestion;

    public QuestionAnsweredEvent(Question question) {
        mQuestion = question;
    }

    public Question getQuestion() {
        return mQuestion;
    }
}
