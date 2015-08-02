package ar.com.wolox.unstuckme.network;

import org.json.JSONObject;

import java.util.List;

import ar.com.wolox.unstuckme.model.Question;
import ar.com.wolox.unstuckme.model.QuestionNew;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface QuestionsService {

    @GET("/api/v1/questions")
    void getQuestions(@Query("page") int page,
                        Callback<List<Question>> db);

    @GET("/api/v1/questions/my_answers")
    void getMyAnswers(
            @Query("page") int page,
            @Query("amount") int amountPerPage,
            Callback<List<Question>> db);

    @GET("/api/v1/questions/my_questions")
    void getMyQuestions(
            @Query("page") int page,
            @Query("amount") int amountPerPage,
            Callback<List<Question>> db);

    @POST("/api/v1/questions")
    void postQuestion(
            @Body QuestionNew body,
            Callback<QuestionNew> cb);
    );
}
