package ar.com.wolox.unstuckme.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ar.com.wolox.unstuckme.Configuration;
import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.UnstuckMeApplication;
import ar.com.wolox.unstuckme.model.Option;
import ar.com.wolox.unstuckme.model.Question;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class QuestionsFragment extends Fragment {

    private List<ImageView> mAnswerImages = new ArrayList<>();
    private List<ImageView> mAnswerImagesTick = new ArrayList<>();

    private int mPageNumber = 0;
    private int mQuestionIndex = 0;
    private List<Question> mQuestionList = new ArrayList<>();

    private Question mQuestionSelected;
    private Option mOptionSelected;

    private Handler mHandler;

    private View.OnClickListener mImageAnswerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getTag() == null) return;
            int tickPos = mAnswerImages.indexOf(view);
            mAnswerImagesTick.get(tickPos).setVisibility(View.VISIBLE);
            getNextQuestionDelayed();
        }
    };

    public static QuestionsFragment newInstance() {
        QuestionsFragment f = new QuestionsFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_questions, container, false);

        setUi(v);
        init();
        setListeners();
        return v;
    }


    private void setUi(View view) {
        mAnswerImages.add( (ImageView) view.findViewById(R.id.questions_imageview_answer_1));
        mAnswerImages.add( (ImageView) view.findViewById(R.id.questions_imageview_answer_2));
        mAnswerImages.add( (ImageView) view.findViewById(R.id.questions_imageview_answer_3));
        mAnswerImages.add( (ImageView) view.findViewById(R.id.questions_imageview_answer_4));

        mAnswerImagesTick.add( (ImageView) view.findViewById(R.id.questions_imageview_answer_tick_1));
        mAnswerImagesTick.add((ImageView) view.findViewById(R.id.questions_imageview_answer_tick_2));
        mAnswerImagesTick.add((ImageView) view.findViewById(R.id.questions_imageview_answer_tick_3));
        mAnswerImagesTick.add((ImageView) view.findViewById(R.id.questions_imageview_answer_tick_4));
    }

    private void init() {
        mHandler = new Handler(Looper.getMainLooper());
        getQuestions();
    }

    private void setListeners() {
        for (View view : mAnswerImages) {
            view.setOnClickListener(mImageAnswerClickListener);
        }
    }

    private void getQuestions() {
        UnstuckMeApplication.sQuestionsService.getQuestions(mPageNumber,
                new Callback<List<Question>>() {
            @Override
            public void success(List<Question> questions, Response response) {
                mQuestionList.addAll(questions);
                populateNextQuestion();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Error", error.toString());
                //Do nothing...
            }
        });
    }

    private void populateNextQuestion() {
        if (getActivity() == null) return;
        int i = 0;
        for (View view : mAnswerImages) {
            view.setVisibility(View.GONE);
        }
        for (View view : mAnswerImagesTick) {
            view.setVisibility(View.GONE);
        }
        for (Option option : mQuestionList.get(mQuestionIndex).getOptions()) {
            Glide.with(this)
                    .load(option.getImageUrl())
                    .centerCrop()
                    .crossFade()
                    .placeholder(null)
                    .into(mAnswerImages.get(i));
            mAnswerImages.get(i).setVisibility(View.VISIBLE);
            mAnswerImagesTick.get(i).setVisibility(View.GONE);
            i++;
        }
        mQuestionIndex++;
    }

    private void getNextQuestionDelayed() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                populateNextQuestion();
            }
        }, Configuration.NEXT_QUESTION_DELAY);
    }
}
