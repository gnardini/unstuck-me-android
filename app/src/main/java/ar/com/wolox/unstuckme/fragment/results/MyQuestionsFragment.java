package ar.com.wolox.unstuckme.fragment.results;

import android.widget.TextView;

import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.network.provider.MyQuestionsProvider;
import ar.com.wolox.unstuckme.network.provider.Provider;

public class MyQuestionsFragment extends ResultsFragment {

    public static MyQuestionsFragment newInstance() {
        MyQuestionsFragment f = new MyQuestionsFragment();
        return f;
    }

    @Override
    protected void init() {
        super.init();
        ((TextView) mNoResultsView).setText(R.string.error_message_no_questions);
    }

    @Override
    protected Provider loadProvider() {
        return new MyQuestionsProvider();
    }
}