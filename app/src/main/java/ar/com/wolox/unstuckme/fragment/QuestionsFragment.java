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

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ar.com.wolox.unstuckme.Configuration;
import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.UnstuckMeApplication;
import ar.com.wolox.unstuckme.listener.OnShareAvailableListener;
import ar.com.wolox.unstuckme.model.Option;
import ar.com.wolox.unstuckme.model.Question;
import ar.com.wolox.unstuckme.model.VotesBatch;
import ar.com.wolox.unstuckme.model.event.LeaveVoteViewEvent;
import ar.com.wolox.unstuckme.model.event.QuestionAnsweredEvent;
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

    private static final int PREFETCHED_IMAGES_COUNT = 5;

    private List<SimpleDraweeView> mAnswerImages = new ArrayList<>();
    private List<ImageView> mAnswerImagesTick = new ArrayList<>();
    private View mNoResults;
    private View mLoadingView;
    private SwipeRefreshLayout mRefreshView;

    private List<Question> mQuestionList = new ArrayList<>();
    private List<Question> mQuestionBackup = new ArrayList<>();
    private List<Question> mVotedQuestions = new ArrayList<>();
    private boolean mWaitingForQuestions = true;
    private boolean mNoMorePages;
    private boolean mCanVote;
    private boolean mFetchingQuestions;

    private ImagePipeline mImagePipeline;
    private int mPrefetchedQuestions;

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
        mImagePipeline = Fresco.getImagePipeline();
        mHandler = new Handler(Looper.getMainLooper());
        mCanVote = true;
        clearViews();
        disablePullToRefresh();
        getQuestions();
    }

    private void setListeners() {
        View view;
        for (int i = 0 ; i < mAnswerImages.size() ; i++) {
            view = mAnswerImages.get(i);
            view.setTag(R.string.question_image_id, i);
            view.setOnClickListener(mImageAnswerClickListener);
        }
        mRefreshView.setOnRefreshListener(this);
    }


    private View.OnClickListener mImageAnswerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int selectedTag = (int) view.getTag(R.string.question_image_id);
            if (mQuestionList == null
                    || mQuestionList.isEmpty()
                    || selectedTag >= mQuestionList.get(0).getOptions().size()
                    || !mCanVote)
                return;
            int tickPos = mAnswerImages.indexOf(view);
            mAnswerImagesTick.get(tickPos).setVisibility(View.VISIBLE);

            AnimationsHelper.startAnimation(getActivity(), view, R.anim.abc_popup_enter,
                    Configuration.NEXT_QUESTION_DELAY);
            populateNextQuestionDelayed();

            voteOption(mQuestionList.get(0).getOptions().get(selectedTag));
            Question removedQuestion = mQuestionList.remove(0);
            mVotedQuestions.add(removedQuestion);
            EventBus.getDefault().post(new QuestionAnsweredEvent(removedQuestion));

            Uri uri;
            for (Option option : removedQuestion.getOptions()) {
                uri = Uri.parse(CloudinaryUtils.getQuestionCompressedImage(option.getImageUrl()));
                mImagePipeline.evictFromCache(uri);
            }
            mPrefetchedQuestions--;
            checkPrefetchedImages();
        }
    };

    private void getQuestions() {
        mFetchingQuestions = true;
        UnstuckMeApplication.sQuestionsService.getQuestions(new Callback<List<Question>>() {
            @Override
            public void success(List<Question> questions, Response response) {
                mFetchingQuestions = false;
                mLoadingView.setVisibility(View.GONE);
                mNoResults.setVisibility(View.GONE);
                disablePullToRefresh();
                if (questions.size() == 0) {
                    setNoMoreQuestions();
                    return;
                }
                if (mWaitingForQuestions) {
                    addQuestionsWithoutDuplicates(mQuestionList, questions);
                    if (mQuestionList.isEmpty()) setNoMoreQuestions();
                    else {
                        mWaitingForQuestions = false;
                        populateNextQuestion();
                        setCanShare(true);
                    }
                } else addQuestionsWithoutDuplicates(mQuestionBackup, questions);

                checkPrefetchedImages();
            }

            @Override
            public void failure(RetrofitError error) {
                mFetchingQuestions = false;
                mNoMorePages = true;
                if (mWaitingForQuestions) {
                    mWaitingForQuestions = false;
                    clearViews();
                    mNoResults.setVisibility(View.VISIBLE);
                    mLoadingView.setVisibility(View.GONE);
                    mRefreshView.setEnabled(true);
                }
            }
        });
    }

    private void populateNextQuestion() {
        if (getActivity() == null) return;

        clearViews();

        if (mQuestionList.isEmpty()) {
            if (mQuestionBackup.isEmpty()) {
                setCanShare(false);
                if (mNoMorePages) {
                    mNoResults.setVisibility(View.VISIBLE);
                    mRefreshView.setEnabled(true);
                } else {
                    mWaitingForQuestions = true;
                    mLoadingView.setVisibility(View.VISIBLE);
                    if (!mFetchingQuestions) getQuestions();
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
        mCanVote = true;
        fetchNextQuestionsIfNecessary();
    }

    private void populateNextQuestionDelayed() {
        mCanVote = false;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                populateNextQuestion();
            }
        }, Configuration.NEXT_QUESTION_DELAY);
    }

    private void checkPrefetchedImages() {
        if (mPrefetchedQuestions < 0) mPrefetchedQuestions = 0;
        while (mPrefetchedQuestions < PREFETCHED_IMAGES_COUNT) {
            Uri uri;
            Question prefetchQuestion = getNextPrefetchableQuestion();
            if (prefetchQuestion == null) return;
            for (Option option : prefetchQuestion.getOptions()) {
                uri = Uri.parse(CloudinaryUtils.getQuestionCompressedImage(option.getImageUrl()));
                mImagePipeline.prefetchToBitmapCache(buildImageRequestFromUri(uri), getActivity());
            }
            mPrefetchedQuestions++;
        }
    }

    private Question getNextPrefetchableQuestion() {
        if (mQuestionList.size() > mPrefetchedQuestions)  {
            return mQuestionList.get(mPrefetchedQuestions);
        }
        int prefetchIndex = mPrefetchedQuestions - mQuestionList.size();
        if (mQuestionBackup.size() > prefetchIndex) {
            return mQuestionBackup.get(prefetchIndex);
        }
        return null;
    }

    private ImageRequest buildImageRequestFromUri(Uri uri) {
        return ImageRequestBuilder
                .newBuilderWithSource(uri)
                .build();
    }

    private void fetchNextQuestionsIfNecessary() {
        if (mQuestionList.size() == Configuration.QUESTIONS_PAGE_THRESHOLD
                || (mQuestionList.size() < Configuration.QUESTIONS_PAGE_THRESHOLD
                    && mQuestionBackup.isEmpty()
                    && !mNoMorePages)) {
            getQuestions();
            sendVotesBatch();
        }
    }

    private void setNoMoreQuestions() {
        mNoMorePages = true;
        if (mWaitingForQuestions) {
            mWaitingForQuestions = false;
            mNoResults.setVisibility(View.VISIBLE);
            mRefreshView.setEnabled(true);
            sendVotesBatch();
            clearViews();
        }
    }

    private void clearViews() {
        for (View view : mAnswerImages) {
            view.setVisibility(View.GONE);
        }
        for (View view : mAnswerImagesTick) {
            view.setVisibility(View.GONE);
        }
    }

    private void addQuestionsWithoutDuplicates(List<Question> dest, List<Question> source) {
        for (Question question : source) {
            if (!mVotedQuestions.contains(question)
                    && !mQuestionList.contains(question)) dest.add(question);
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
        mWaitingForQuestions = true;
        mNoMorePages = false;
        getQuestions();
    }

    private void disablePullToRefresh() {
        mRefreshView.setEnabled(false);
        if (mRefreshView.isRefreshing()) mRefreshView.setRefreshing(false);
    }

    private void setCanShare(boolean canShare) {
        ((OnShareAvailableListener) getActivity()).canShare(canShare);
    }

    public boolean canShare() {
        return !mQuestionList.isEmpty();
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
