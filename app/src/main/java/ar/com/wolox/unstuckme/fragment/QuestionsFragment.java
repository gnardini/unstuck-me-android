package ar.com.wolox.unstuckme.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import ar.com.wolox.unstuckme.model.event.ShareEvent;
import ar.com.wolox.unstuckme.utils.CloudinaryUtils;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class QuestionsFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener {

    private static final String PLAIN_TEXT = "text/plain";
    private static final String SHARE_QUESTION_ID = "share_question_id";

    private List<ImageView> mAnswerImages = new ArrayList<>();
    private List<ImageView> mAnswerImagesTick = new ArrayList<>();
    private View mNoResults;
    private View mLoadingView;
    private SwipeRefreshLayout mRefreshView;

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

        mNoResults = view.findViewById(R.id.no_results);
        mLoadingView = view.findViewById(R.id.questions_loading);
        mRefreshView = (SwipeRefreshLayout) view.findViewById(R.id.refresh_view);
    }

    private void init() {
        mHandler = new Handler(Looper.getMainLooper());
        getQuestions();
    }

    private void setListeners() {
        for (View view : mAnswerImages) {
            view.setOnClickListener(mImageAnswerClickListener);
        }
        mRefreshView.setOnRefreshListener(this);
    }

    private void getQuestions() {
        UnstuckMeApplication.sQuestionsService.getQuestions(new Callback<List<Question>>() {
            @Override
            public void success(List<Question> questions, Response response) {
                mLoadingView.setVisibility(View.GONE);
                mRefreshView.setRefreshing(false);
                if (questions.size() == 0) {
                    mNoMorePages = true;
                    sendVotesBatch();
                    if (mWaitingForQuestions) mNoResults.setVisibility(View.VISIBLE);
                    return;
                }
                addQuestionsWithoutDuplicates(questions);
                if (mWaitingForQuestions) {
                    mWaitingForQuestions = false;
                    populateNextQuestion();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Error", error.toString());
                mNoResults.setVisibility(View.VISIBLE);
                mLoadingView.setVisibility(View.GONE);
                mRefreshView.setRefreshing(false);
            }
        });
    }

    private void populateNextQuestion() {
        if (getActivity() == null) return;

        if (mQuestionIndex == null) mQuestionIndex = 0;
        else mQuestionIndex++;

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
            if (mNoMorePages) mNoResults.setVisibility(View.VISIBLE);
            else mLoadingView.setVisibility(View.VISIBLE);
            if (mQuestionIndex < Configuration.QUESTIONS_PAGE_THRESHOLD) getQuestions();
            return;
        }

        //Populate image views
        int i = 0;
        for (Option option : mQuestionList.get(mQuestionIndex).getOptions()) {
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

    @Override
    public void onStop() {
        sendVotesBatch();
        super.onStop();
    }

    private void sendVotesBatch() {
        if (mVotesList.size() == 0) return;
        VotesBatch votesBatch = new VotesBatch(mVotesList);
        UnstuckMeApplication.sQuestionsService.sendVotes(votesBatch,
                new Callback<Void>() {

            @Override
            public void success(Void aVoid, Response response) {
                //Do nothing...
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Votes Error:", error.toString());
                //Do nothing...
            }
        });

        mVotesList = new ArrayList<>();
    }

    @Override
    public void onRefresh() {
        mNoResults.setVisibility(View.GONE);
        mRefreshView.setRefreshing(true);
        getQuestions();
    }

    public void onEvent(ShareEvent event) {
        if (mQuestionList == null
                || mQuestionIndex == null
                || mQuestionList.size() <= mQuestionIndex)
            return;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, Configuration.API_ENDPOINT
                + Configuration.ADD_QUERY
                + mQuestionList.get(mQuestionIndex).getId());
        sendIntent.setType(PLAIN_TEXT);
        getActivity().startActivity(sendIntent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
