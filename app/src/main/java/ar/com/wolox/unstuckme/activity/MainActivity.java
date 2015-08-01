package ar.com.wolox.unstuckme.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.fragment.QuestionsFragment;
import ar.com.wolox.unstuckme.fragment.CreateQuestionsFragment;
import ar.com.wolox.unstuckme.fragment.ResultsFragment;

public class MainActivity extends FragmentActivity {

    private final static int POSITION_RESULTS = 0;
    private final static int POSITION_ANSWER = 1;
    private final static int POSITION_CREATE = 2;

    private View mResultsTab;
    private View mAnswerTab;
    private View mCreateQuestionTab;

    private ResultsFragment mResultsFragment;
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
        mResultsTab = findViewById(R.id.main_tab_results);
        mAnswerTab = findViewById(R.id.main_tab_answer);
        mCreateQuestionTab = findViewById(R.id.main_tab_create);
    }

    private void init() {
        mResultsTab.setTag(POSITION_RESULTS);
        mAnswerTab.setTag(POSITION_ANSWER);
        mCreateQuestionTab.setTag(POSITION_CREATE);

        mResultsFragment = ResultsFragment.newInstance();
        mQuestionsFragment = QuestionsFragment.newInstance();
        mCreateQuestionsFragment = CreateQuestionsFragment.newInstance();
        setFragment(POSITION_RESULTS);
    }

    private void setListeners() {
        View.OnClickListener onTabClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment((int) view.getTag());
            }
        };
        mResultsTab.setOnClickListener(onTabClickListener);
        mAnswerTab.setOnClickListener(onTabClickListener);
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
            case POSITION_RESULTS:
                return mResultsFragment;
            case POSITION_ANSWER:
                return mQuestionsFragment;
            case POSITION_CREATE:
                return mCreateQuestionsFragment;
        }
        return null;
    }
}
