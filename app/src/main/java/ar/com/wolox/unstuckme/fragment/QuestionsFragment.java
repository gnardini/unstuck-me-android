package ar.com.wolox.unstuckme.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ar.com.wolox.unstuckme.Configuration;
import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.UnstuckMeApplication;
import ar.com.wolox.unstuckme.model.Option;
import ar.com.wolox.unstuckme.model.Question;
import ar.com.wolox.unstuckme.model.VotesBatch;
import ar.com.wolox.unstuckme.model.event.LeaveVoteViewEvent;
import ar.com.wolox.unstuckme.model.event.ShareEvent;
import ar.com.wolox.unstuckme.model.event.VotesSentEvent;
import ar.com.wolox.unstuckme.network.share.ShareObject;
import ar.com.wolox.unstuckme.utils.AnimationsHelper;
import ar.com.wolox.unstuckme.utils.CloudinaryUtils;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class QuestionsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private List<SimpleDraweeView> mAnswerImages = new ArrayList<>();
    private List<ImageView> mAnswerImagesTick = new ArrayList<>();
    private View mNoResults;
    private View mLoadingView;
    private SwipeRefreshLayout mRefreshView;
    private ImageView mSpinner;

    private List<Question> mQuestionList = new ArrayList<>();
    private List<Question> mQuestionBackup = new ArrayList<>();
    private List<Question> mVotedQuestions = new ArrayList<>();
    private boolean mWaitingForQuestions = true;
    private boolean mNoMorePages;
    private boolean mCanVote;

    private List<Integer> mVotesList = new ArrayList<>();

    private Handler mHandler;


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
        mAnswerImages.add( (SimpleDraweeView) view.findViewById(R.id.questions_imageview_answer_1));
        mAnswerImages.add( (SimpleDraweeView) view.findViewById(R.id.questions_imageview_answer_2));
        mAnswerImages.add( (SimpleDraweeView) view.findViewById(R.id.questions_imageview_answer_3));
        mAnswerImages.add( (SimpleDraweeView) view.findViewById(R.id.questions_imageview_answer_4));

        mAnswerImagesTick.add((ImageView) view.findViewById(R.id.questions_imageview_answer_tick_1));
        mAnswerImagesTick.add((ImageView) view.findViewById(R.id.questions_imageview_answer_tick_2));
        mAnswerImagesTick.add((ImageView) view.findViewById(R.id.questions_imageview_answer_tick_3));
        mAnswerImagesTick.add((ImageView) view.findViewById(R.id.questions_imageview_answer_tick_4));

        mNoResults = view.findViewById(R.id.no_results);
        mLoadingView = view.findViewById(R.id.questions_loading);
        mRefreshView = (SwipeRefreshLayout) view.findViewById(R.id.refresh_view);
    }

    private void init() {
        mHandler = new Handler(Looper.getMainLooper());
        mCanVote = true;
        clearViews();
        getQuestions();
    }

    private void setListeners() {
        for (View view : mAnswerImages) {
            view.setOnClickListener(mImageAnswerClickListener);
        }
        mRefreshView.setOnRefreshListener(this);
    }


    private View.OnClickListener mImageAnswerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mQuestionList == null
                    || mQuestionList.isEmpty()
                    || !mCanVote)
                return;
            int tickPos = mAnswerImages.indexOf(view);
            mAnswerImagesTick.get(tickPos).setVisibility(View.VISIBLE);

            AnimationsHelper.startAnimation(getActivity(), view, R.anim.abc_popup_enter,
                    Configuration.NEXT_QUESTION_DELAY);
            populateNextQuestionDelayed();

            switch (view.getId()) {
                case R.id.questions_imageview_answer_1:
                    voteOption(mQuestionList.get(0).getOptions().get(0));
                    break;
                case R.id.questions_imageview_answer_2:
                    voteOption(mQuestionList.get(0).getOptions().get(1));
                    break;
                case R.id.questions_imageview_answer_3:
                    voteOption(mQuestionList.get(0).getOptions().get(2));
                    break;
                case R.id.questions_imageview_answer_4:
                    voteOption(mQuestionList.get(0).getOptions().get(3));
                    break;
            }
            mVotedQuestions.add(mQuestionList.remove(0));
        }
    };

    private void getQuestions() {
        UnstuckMeApplication.sQuestionsService.getQuestions(new Callback<List<Question>>() {
            @Override
            public void success(List<Question> questions, Response response) {
                mLoadingView.setVisibility(View.GONE);
                mNoResults.setVisibility(View.GONE);
                mRefreshView.setVisibility(View.GONE);
                if (questions.size() == 0) {
                    mNoMorePages = true;
                    if (mWaitingForQuestions) {
                        mWaitingForQuestions = false;
                        mNoResults.setVisibility(View.VISIBLE);
                        mRefreshView.setVisibility(View.VISIBLE);
                        sendVotesBatch();
                        clearViews();
                    }
                    return;
                }
                if (mWaitingForQuestions) {
                    addQuestionsWithoutDuplicates(mQuestionList, questions);
                    populateNextQuestion();
                } else addQuestionsWithoutDuplicates(mQuestionBackup, questions);
            }

            @Override
            public void failure(RetrofitError error) {
                mNoMorePages = true;
                if (mWaitingForQuestions) {
                    mWaitingForQuestions = false;
                    clearViews();
                    mNoResults.setVisibility(View.VISIBLE);
                    mLoadingView.setVisibility(View.GONE);
                    mRefreshView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void populateNextQuestion() {
        if (getActivity() == null) return;

        clearViews();

        //If this is the last question do nothing, wait and cry
        if (mQuestionList.isEmpty()) {
            if (mQuestionBackup.isEmpty()) {
                if (mNoMorePages) {
                    mNoResults.setVisibility(View.VISIBLE);
                    mRefreshView.setVisibility(View.VISIBLE);
                } else {
                    mWaitingForQuestions = true;
                    mLoadingView.setVisibility(View.VISIBLE);
                }
                return;
            } else {
                mQuestionList.addAll(mQuestionBackup);
                mQuestionBackup.clear();
            }
        }

        //Populate image views
        int i = 0;
        Question question = mQuestionList.get(0);
        for (Option option : question.getOptions()) {
            Uri uri = Uri.parse(CloudinaryUtils.getQuestionCompressedImage(option.getImageUrl()));
            mAnswerImages.get(i).setImageURI(uri);
            mAnswerImages.get(i).setVisibility(View.VISIBLE);
            mAnswerImagesTick.get(i).setVisibility(View.GONE);
            i++;
        }
        fetchNextQuestionsIfNecessary();
    }

    private void populateNextQuestionDelayed() {
        mCanVote = false;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                populateNextQuestion();
                mCanVote = true;
            }
        }, Configuration.NEXT_QUESTION_DELAY);
    }

    private void fetchNextQuestionsIfNecessary() {
        if (mQuestionList.size() == Configuration.QUESTIONS_PAGE_THRESHOLD) {
            getQuestions();
            sendVotesBatch();
        }
    }

    private void clearViews() {
        for (DraweeView draweeView : mAnswerImages) {
            draweeView.setVisibility(View.GONE);
        }
        for (View view : mAnswerImagesTick) {
            view.setVisibility(View.GONE);
        }
    }

    private void addQuestionsWithoutDuplicates(List<Question> dest, List<Question> source) {
        for (Question question : source) {
            if (!mVotedQuestions.contains(question)) dest.add(question);
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
        final int size = mVotesList.size();
        final List<Integer> deepCopy = getDeepCopy(mVotesList);
        VotesBatch votesBatch = new VotesBatch(deepCopy);
        UnstuckMeApplication.sQuestionsService.sendVotes(votesBatch,
                new Callback<Void>() {

                    @Override
                    public void success(Void aVoid, Response response) {
                        mVotesList.removeAll(deepCopy);
                        EventBus.getDefault().post(new VotesSentEvent(size));
                    }

                    @Override
                    public void failure(RetrofitError error) {
                    }
                });

        mVotesList = new ArrayList<>();
    }

    // Just being paranoid here
    private List<Integer> getDeepCopy(List<Integer> list) {
        List<Integer> copy = new LinkedList<>();
        for (Integer integer : list) copy.add(integer.intValue());
        return copy;
    }

    @Override
    public void onRefresh() {
        mNoResults.setVisibility(View.GONE);
        getQuestions();
    }

    public void onEvent(ShareEvent event) {
        if (mQuestionList == null || mQuestionList.isEmpty()) return;
        new ShareObject(getActivity(), mQuestionList.get(0).getId()).share();
    }

    public void onEvent(LeaveVoteViewEvent event) {
        sendVotesBatch();
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
