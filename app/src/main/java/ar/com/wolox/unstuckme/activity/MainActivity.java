package ar.com.wolox.unstuckme.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.fragment.QuestionsFragment;
import ar.com.wolox.unstuckme.fragment.CreateQuestionsFragment;
import ar.com.wolox.unstuckme.fragment.QuestionsFragment;

public class MainActivity extends FragmentActivity {

    private final static int POSITION_ANSWERS = 0;
    private final static int POSITION_QUESTIONS = 1;
    private final static int POSITION_CREATE_QUESTIONS = 2;

    private View mAnswersTab;
    private View mQuestionsTab;
    private View mCreateQuestionTab;

    private AnswersFragment mAnswersFragment;

    private QuestionsFragment mQuestionsFragment;
    private CreateQuestionsFragment mCreateQuestionsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUi();
        init();
        setListeners();
    }

    private void setUi() {
        mAnswersTab = findViewById(R.id.main_tab_answers);
        mQuestionsTab = findViewById(R.id.main_tab_questions);
        mCreateQuestionTab = findViewById(R.id.main_tab_create_question);
    }

    private void init() {
        mAnswersTab.setTag(POSITION_ANSWERS);
        mQuestionsTab.setTag(POSITION_QUESTIONS);
        mCreateQuestionTab.setTag(POSITION_CREATE_QUESTIONS);

        mAnswersFragment = AnswersFragment.newInstance();
        mQuestionsFragment = QuestionsFragment.newInstance();
        mCreateQuestionsFragment = CreateQuestionsFragment.newInstance();
        setFragment(POSITION_QUESTIONS);
    }

    private void setListeners() {
        View.OnClickListener onTabClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment((int) view.getTag());
            }
        };
        mAnswersTab.setOnClickListener(onTabClickListener);
        mQuestionsTab.setOnClickListener(onTabClickListener);
        mCreateQuestionTab.setOnClickListener(onTabClickListener);
    }

    private void setFragment(int position) {
        Fragment fragment = getFragment(position);
        if (fragment == null) return;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_main_container, fragment)
                .commit();
    }

    private Fragment getFragment(int position) {
        switch (position) {
            case POSITION_ANSWERS:
                return mAnswersFragment;
            case POSITION_QUESTIONS:
                return mQuestionsFragment;
            case POSITION_CREATE_QUESTIONS:
                return mCreateQuestionsFragment;
        }
        return null;
    }
}
