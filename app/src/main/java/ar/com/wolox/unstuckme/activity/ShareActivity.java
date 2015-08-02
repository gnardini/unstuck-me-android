package ar.com.wolox.unstuckme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.UnstuckMeApplication;
import ar.com.wolox.unstuckme.model.Option;
import ar.com.wolox.unstuckme.model.Question;
import ar.com.wolox.unstuckme.model.VotesBatch;
import ar.com.wolox.unstuckme.utils.CloudinaryUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ShareActivity extends FragmentActivity {

    private List<ImageView> mAnswerImages = new ArrayList<>();
    private List<ImageView> mAnswerImagesTick = new ArrayList<>();
    private View mGoToApp;

    private Question mQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        setUi();
        init();
        setListeners();
    }

    private void setUi() {
        mAnswerImages.add( (ImageView) findViewById(R.id.questions_imageview_answer_1));
        mAnswerImages.add( (ImageView) findViewById(R.id.questions_imageview_answer_2));
        mAnswerImages.add((ImageView) findViewById(R.id.questions_imageview_answer_3));
        mAnswerImages.add((ImageView) findViewById(R.id.questions_imageview_answer_4));

        mAnswerImagesTick.add((ImageView) findViewById(R.id.questions_imageview_answer_tick_1));
        mAnswerImagesTick.add((ImageView) findViewById(R.id.questions_imageview_answer_tick_2));
        mAnswerImagesTick.add((ImageView) findViewById(R.id.questions_imageview_answer_tick_3));
        mAnswerImagesTick.add((ImageView) findViewById(R.id.questions_imageview_answer_tick_4));

        mGoToApp = findViewById(R.id.share_goto_app);
    }

    private void init() {
        int questionId = Integer.valueOf(getIntent().getData().getQuery());
        UnstuckMeApplication.sQuestionsService.getQuestion(questionId, new Callback<Question>() {
            @Override
            public void success(Question question, Response response) {
                mQuestion = question;
                populate(question);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(ShareActivity.this,
                        getString(R.string.error_share_view), Toast.LENGTH_SHORT);
                goToMainActivity();
            }
        });
    }

    private void setListeners() {
        for (View view : mAnswerImages) {
            view.setOnClickListener(mImageAnswerClickListener);
        }
        mGoToApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMainActivity();
            }
        });
    }

    private void populate(Question question) {
        int i = 0;
        for (Option option : question.getOptions()) {
            Glide.with(this)
                    .load(CloudinaryUtils.getQuestionCompressedImage(option.getImageUrl()))
                    .centerCrop()
                    .crossFade()
                    .placeholder(null)
                    .into(mAnswerImages.get(i));
            mAnswerImages.get(i).setVisibility(View.VISIBLE);
            mAnswerImagesTick.get(i).setVisibility(View.GONE);
            i++;
        }
    }

    private View.OnClickListener mImageAnswerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mQuestion == null) return;
            int tickPos = mAnswerImages.indexOf(view);
            mAnswerImagesTick.get(tickPos).setVisibility(View.VISIBLE);

            switch (view.getId()) {
                case R.id.questions_imageview_answer_1:
                    voteOption(mQuestion.getOptions().get(0));
                    break;
                case R.id.questions_imageview_answer_2:
                    voteOption(mQuestion.getOptions().get(1));
                    break;
                case R.id.questions_imageview_answer_3:
                    voteOption(mQuestion.getOptions().get(2));
                    break;
                case R.id.questions_imageview_answer_4:
                    voteOption(mQuestion.getOptions().get(3));
                    break;
            }
        }
    };

    private void voteOption(Option option) {
        List<Integer> votes = new LinkedList<>();
        votes.add(option.getId());
        UnstuckMeApplication.sQuestionsService.sendVotes(new VotesBatch(votes),
                    new Callback<Void>() {
                        @Override
                        public void success(Void aVoid, Response response) {
                            goToMainActivity();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            goToMainActivity();
                        }
                    });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}