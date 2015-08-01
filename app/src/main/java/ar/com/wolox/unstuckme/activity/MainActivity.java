package ar.com.wolox.unstuckme.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import ar.com.wolox.unstuckme.R;

public class MainActivity extends FragmentActivity {

    private final static int POSITION_RESULTS = 0;
    private final static int POSITION_ANSWER = 1;
    private final static int POSITION_CREATE = 2;

    private View mResultsTab;
    private View mAnswerTab;
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
        mResultsTab = findViewById(R.id.main_tab_results);
        mAnswerTab = findViewById(R.id.main_tab_answer);
        mCreateQuestionTab = findViewById(R.id.main_tab_create);
    }

    private void init() {
        mResultsTab.setTag(POSITION_RESULTS);
        mAnswerTab.setTag(POSITION_ANSWER);
        mCreateQuestionTab.setTag(POSITION_CREATE);
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
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_main_container, fragment)
                .commit();
    }

    private Fragment getFragment(int position) {
        switch (position) {
            case POSITION_RESULTS:
                return null;
            case POSITION_ANSWER:
                return null;
            case POSITION_CREATE:
                return null;
        }
    }
}
