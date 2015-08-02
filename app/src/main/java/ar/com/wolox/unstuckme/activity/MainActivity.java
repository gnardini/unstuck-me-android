package ar.com.wolox.unstuckme.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;

import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.UnstuckMeApplication;
import ar.com.wolox.unstuckme.adapter.MainAdapter;
import ar.com.wolox.unstuckme.fragment.AnswersFragment;
import ar.com.wolox.unstuckme.fragment.create_question.CreateQuestionsContainerFragment;
import ar.com.wolox.unstuckme.fragment.QuestionsFragment;
import ar.com.wolox.unstuckme.utils.QuestionBuilder;

public class MainActivity extends FragmentActivity {

    private final static int POSITION_ANSWERS = 0;
    private final static int POSITION_QUESTIONS = 1;
    private final static int POSITION_CREATE_QUESTIONS = 2;

    private ViewPager mViewPager;
    private MainAdapter mMainAdapter;
    private View mAnswersTab;
    private View mQuestionsTab;
    private View mCreateQuestionTab;

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
    }

    private void init() {
        mAnswersTab.setTag(POSITION_ANSWERS);
        mQuestionsTab.setTag(POSITION_QUESTIONS);
        mCreateQuestionTab.setTag(POSITION_CREATE_QUESTIONS);

        mMainAdapter = new MainAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mMainAdapter);
        mViewPager.setOffscreenPageLimit(MainAdapter.TABS_COUNT);
        setTabSelected(mQuestionsTab);

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
        mViewPager.setCurrentItem(position);
    }
}
