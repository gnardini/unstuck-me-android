package ar.com.wolox.unstuckme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import ar.com.wolox.unstuckme.Configuration;
import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.UnstuckMeApplication;
import ar.com.wolox.unstuckme.adapter.MainAdapter;
import ar.com.wolox.unstuckme.listener.OnCreditsAddedListener;
import ar.com.wolox.unstuckme.listener.OnShareAvailableListener;
import ar.com.wolox.unstuckme.model.User;
import ar.com.wolox.unstuckme.model.event.LeaveVoteViewEvent;
import ar.com.wolox.unstuckme.model.event.ShareEvent;
import ar.com.wolox.unstuckme.network.notification.PushReceiver;
import ar.com.wolox.unstuckme.utils.AccessUtils;
import ar.com.wolox.unstuckme.utils.QuestionBuilder;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends FragmentActivity implements
        OnShareAvailableListener,
        OnCreditsAddedListener {

    private final static int POSITION_ANSWERS = 0;
    private final static int POSITION_QUESTIONS = 1;
    private final static int POSITION_CREATE_QUESTIONS = 2;

    private ViewPager mViewPager;
    private MainAdapter mMainAdapter;
    private View mAnswersTab;
    private View mQuestionsTab;
    private View mCreateQuestionTab;
    private TextView mCredits;
    private View mShare;
    private View mProfile;

    private User mUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUi();
        init();
        setListeners();
    }

    private void setUi() {
        mViewPager = (ViewPager) findViewById(R.id.main_view_pager);
        mAnswersTab = findViewById(R.id.main_tab_answers);
        mQuestionsTab = findViewById(R.id.main_tab_questions);
        mCreateQuestionTab = findViewById(R.id.main_tab_create_question);
        mCredits = (TextView) findViewById(R.id.toolbar_credits);
        mShare = findViewById(R.id.toolbar_share);
        mProfile = findViewById(R.id.toolbar_user);
    }

    private void init() {
        mAnswersTab.setTag(POSITION_ANSWERS);
        mQuestionsTab.setTag(POSITION_QUESTIONS);
        mCreateQuestionTab.setTag(POSITION_CREATE_QUESTIONS);

        getCredits();

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(PushReceiver.QUESTION_ID)) {
            int questionId = (int) extras.get(PushReceiver.QUESTION_ID);
            if (questionId != Configuration.QUESTION_ID_ERROR) {
                pushNotificationInit(questionId);
                return;
            }
        }
        commonInit(Configuration.QUESTION_ID_ERROR, mQuestionsTab);
    }

    private void pushNotificationInit(int questionId) {
        commonInit(questionId, mAnswersTab);
    }

    private void commonInit(int pushQuestionId, View selectedTab) {
        mMainAdapter = new MainAdapter(getSupportFragmentManager(), this, pushQuestionId);
        mViewPager.setAdapter(mMainAdapter);
        mViewPager.setOffscreenPageLimit(MainAdapter.TABS_COUNT);
        setTabSelected(selectedTab);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(PushReceiver.LEVEL_UP)) {
            startActivity(new Intent(this, UserActivity.class));
            return;
        }
    }

    private void getCredits() {
        mUser = AccessUtils.getLoggedUser();
        if (mUser != null) {
            mCredits.setText(String.valueOf(mUser.getCredits()));
        } else {
            UnstuckMeApplication.sUserService.getUserStats(new Callback<User>() {
                @Override
                public void success(User user, Response response) {
                    AccessUtils.updateUser(user);
                    mUser = user;
                    mCredits.setText(String.valueOf(user.getCredits()));
                }

                @Override
                public void failure(RetrofitError error) {
                }
            });
        }
    }

    public void addCredits(int credits) {
        if (mUser != null) {
            mUser.addCredits(credits);
            mCredits.setText(String.valueOf(mUser.getCredits()));
            AccessUtils.updateCredits(mUser);
        }
    }

    private void setListeners() {
        View.OnClickListener onTabClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTabSelected(view);
            }
        };
        mAnswersTab.setOnClickListener(onTabClickListener);
        mQuestionsTab.setOnClickListener(onTabClickListener);
        mCreateQuestionTab.setOnClickListener(onTabClickListener);

        mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new ShareEvent());
                EventBus.getDefault().post(new LeaveVoteViewEvent());
            }
        });
        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UserActivity.class));
                EventBus.getDefault().post(new LeaveVoteViewEvent());
            }
        });
    }

    private void setTabSelected(View view) {
        int position = (int) view.getTag();
        mAnswersTab.setSelected(position == POSITION_ANSWERS);
        mQuestionsTab.setSelected(position == POSITION_QUESTIONS);
        mCreateQuestionTab.setSelected(position == POSITION_CREATE_QUESTIONS);
        if (position == POSITION_CREATE_QUESTIONS) {
            QuestionBuilder.resetBuilder();
            FragmentManager fm = getSupportFragmentManager();
            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
        }
        canShare(position == POSITION_QUESTIONS && mMainAdapter.canShare());
        mViewPager.setCurrentItem(position);
        if (position != POSITION_QUESTIONS) EventBus.getDefault().post(new LeaveVoteViewEvent());
    }

    @Override
    public void canShare(boolean canShare) {
        mShare.setVisibility(canShare ? View.VISIBLE : View.GONE);
    }
}
