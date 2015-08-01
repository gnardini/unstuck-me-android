package ar.com.wolox.unstuckme.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.fragment.AnswersFragment;
import ar.com.wolox.unstuckme.fragment.CreateQuestionsFragment;

public class MainActivity extends FragmentActivity {

    private final static int POSITION_ANSWERS = 0;
    private final static int POSITION_QUESTIONS = 1;
    private final static int POSITION_CREATE_QUESTIONS = 2;

    private View mAnswersTab;
    private View mQuestionsTab;
    private View mCreateQuestionTab;

    private AnswersFragment mAnswersFragment;

    private AnswersFragment mQuestionsFragment;
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
        mQuestionsFragment = AnswersFragment.newInstance();
        mCreateQuestionsFragment = CreateQuestionsFragment.newInstance();
        setFragment(POSITION_QUESTIONS);
        setTabSelected(mQuestionsTab);
    }

    private void setListeners() {
        View.OnClickListener onTabClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTabSelected(view);
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
                .setCustomAnimations(R.anim.abc_grow_fade_in_from_bottom,
                        R.anim.abc_shrink_fade_out_from_bottom)
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

    private void setTabSelected(View view) {
        switch ((int) view.getTag()) {
            case POSITION_ANSWERS:
                mAnswersTab.setSelected(true);
                mQuestionsTab.setSelected(false);
                mCreateQuestionTab.setSelected(false);
                break;
            case POSITION_QUESTIONS:
                mAnswersTab.setSelected(false);
                mQuestionsTab.setSelected(true);
                mCreateQuestionTab.setSelected(false);
                break;
            case POSITION_CREATE_QUESTIONS:
                mAnswersTab.setSelected(false);
                mQuestionsTab.setSelected(false);
                mCreateQuestionTab.setSelected(true);
                break;
        }
    }
}
