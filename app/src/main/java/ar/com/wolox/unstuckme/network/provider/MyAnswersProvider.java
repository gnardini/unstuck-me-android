package ar.com.wolox.unstuckme.network.provider;

import java.util.List;

import ar.com.wolox.unstuckme.UnstuckMeApplication;
import ar.com.wolox.unstuckme.model.Question;
import retrofit.Callback;

public class MyAnswersProvider implements Provider<Question> {

    @Override
    public void provide(int currentPage, int itemsPerPage, Callback<List<Question>> callback) {
        UnstuckMeApplication.sQuestionsService.getMyAnswers(currentPage, itemsPerPage, callback);
    }
}
