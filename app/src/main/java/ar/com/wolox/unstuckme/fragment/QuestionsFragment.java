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
import ar.com.wolox.unstuckme.model.VotesBatch;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class QuestionsFragment extends Fragment {

    private List<ImageView> mAnswerImages = new ArrayList<>();
    private List<ImageView> mAnswerImagesTick = new ArrayList<>();

    private Integer mQuestionIndex = null;
    private List<Question> mQuestionList = new ArrayList<>();
    private boolean mWaitingForQuestions = true;
    private boolean mNoMorePages = false;

    private List<Integer> mVotesList = new ArrayList<>();

    private Handler mHandler;

    private View.OnClickListener mImageAnswerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int tickPos = mAnswerImages.indexOf(view);
            mAnswerImagesTick.get(tickPos).setVisibility(View.VISIBLE);
            populateNextQuestionDelayed();

            switch (view.getId()) {
                case R.id.questions_imageview_answer_1:
                    voteOption(mQuestionList.get(mQuestionIndex).getOptions().get(0));
                    break;
                case R.id.questions_imageview_answer_2:
                    voteOption(mQuestionList.get(mQuestionIndex).getOptions().get(1));
                    break;
                case R.id.questions_imageview_answer_3:
                    voteOption(mQuestionList.get(mQuestionIndex).getOptions().get(2));
                    break;
                case R.id.questions_imageview_answer_4:
                    voteOption(mQuestionList.get(mQuestionIndex).getOptions().get(3));
                    break;
            }
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
        UnstuckMeApplication.sQuestionsService.getQuestions(new Callback<List<Question>>() {
            @Override
            public void success(List<Question> questions, Response response) {
                if (questions.size() == 0) {
                    mNoMorePages = true;
                    return;
                }
                addQuestionsWithoutDuplicates(questions);
                if (mWaitingForQuestions) populateNextQuestion();
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

        if (mQuestionIndex == null)
            mQuestionIndex = 0;
        else
            mQuestionIndex++;

        //Clean views
        for (View view : mAnswerImages) {
            view.setVisibility(View.GONE);
        }
        for (View view : mAnswerImagesTick) {
            view.setVisibility(View.GONE);
        }

        //If this is the last question do nothing, wait and cry
        if (mQuestionList.size() == mQuestionIndex) {
            mWaitingForQuestions = true;
            return;
        }

        //Populate image views
        int i = 0;
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
        fetchNextQuestionsIfNecessary();
    }

    private void populateNextQuestionDelayed() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                populateNextQuestion();
            }
        }, Configuration.NEXT_QUESTION_DELAY);
    }

    private void fetchNextQuestionsIfNecessary() {
        if ( ((mQuestionList.size() -1 ) - mQuestionIndex)
                == Configuration.QUESTIONS_PAGE_THRESHOLD) {
            getQuestions();
            sendVotesBatch();
        }
    }

    private void addQuestionsWithoutDuplicates(List<Question> questionList) {
        for (Question question : questionList) {
            if (!mQuestionList.contains(question)) mQuestionList.add(question);
        }
    }

    private void voteOption(Option option) {
        mVotesList.add(option.getId());
    }

    private void sendVotesBatch() {
        if (mVotesList.size() == 0) return;
        VotesBatch votesBatch = new VotesBatch(mVotesList);
        UnstuckMeApplication.sQuestionsService.sendVotes(votesBatch,
                new Callback<List<Question>>() {
            @Override
            public void success(List<Question> questionList, Response response) {
                int a = 0;
                //Do nothing...
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Error:", error.toString());
                int a = 0;
                //Do nothing...
            }
        });
        mVotesList = new ArrayList<>();
    }
}
