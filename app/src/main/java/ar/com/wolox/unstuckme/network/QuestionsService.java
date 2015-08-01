package ar.com.wolox.unstuckme.network;

import java.util.List;

import ar.com.wolox.unstuckme.model.Question;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface QuestionsService {

    @GET("/api/v1/questions")
    void getQuestions(@Query("page") int page,
                        Callback<List<Question>> db);

    @GET("/api/v1/questions/my_answers")
    void getMyAnswers(
            @Query("user") String user,
            @Query("page") int page,
            @Query("amount") int amountPerPage,
            Callback<List<Question>> db);

    @GET("/api/v1/questions/my_questions")
    void getMyQuestions(
            @Query("user") String user,
            @Query("page") int page,
            @Query("amount") int amountPerPage,
            Callback<List<Question>> db);
}
