package ar.com.wolox.unstuckme.network.provider;

import java.util.List;

import ar.com.wolox.unstuckme.UnstuckMeApplication;
import ar.com.wolox.unstuckme.model.Question;
import ar.com.wolox.unstuckme.utils.AccessUtils;
import retrofit.Callback;

public class MyQuestionsProvider implements Provider<Question> {

    @Override
    public void provide(int currentPage, int itemsPerPage, Callback<List<Question>> callback) {
        UnstuckMeApplication.sQuestionsService.getMyQuestions(AccessUtils.getDeviceId(),
                currentPage, itemsPerPage, callback);
    }
}
