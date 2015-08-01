package ar.com.wolox.unstuckme.network;

import java.util.List;

import ar.com.wolox.unstuckme.model.Question;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface QuestionsService {

    @GET("/questions")
    void getQuestions(@Query("page") int page,
                        Callback<List<Question>> db);
}
