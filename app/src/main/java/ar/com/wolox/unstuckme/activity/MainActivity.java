package ar.com.wolox.unstuckme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;

import ar.com.wolox.unstuckme.Configuration;
import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.adapter.MainAdapter;
import ar.com.wolox.unstuckme.model.event.LeaveVoteViewEvent;
import ar.com.wolox.unstuckme.model.event.ShareEvent;
import ar.com.wolox.unstuckme.network.notification.PushReceiver;
import ar.com.wolox.unstuckme.utils.QuestionBuilder;
import de.greenrobot.event.EventBus;

public class MainActivity extends FragmentActivity {

    private final static int POSITION_ANSWERS = 0;
    private final static int POSITION_QUESTIONS = 1;
    private final static int POSITION_CREATE_QUESTIONS = 2;

    private ViewPager mViewPager;
    private MainAdapter mMainAdapter;
    private View mAnswersTab;
    private View mQuestionsTab;
    private View mCreateQuestionTab;
    private View mShare;
    private View mProfile;

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
        mShare = findViewById(R.id.toolbar_share);
        mProfile = findViewById(R.id.toolbar_user);
    }

    private void init() {
        mAnswersTab.setTag(POSITION_ANSWERS);
        mQuestionsTab.setTag(POSITION_QUESTIONS);
        mCreateQuestionTab.setTag(POSITION_CREATE_QUESTIONS);

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
        mShare.setVisibility(position == POSITION_QUESTIONS ? View.VISIBLE : View.GONE);
        mViewPager.setCurrentItem(position);
        if (position != POSITION_QUESTIONS) EventBus.getDefault().post(new LeaveVoteViewEvent());
    }
}
