package ar.com.wolox.unstuckme.fragment.results;

import android.widget.TextView;

import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.network.provider.MyAnswersProvider;
import ar.com.wolox.unstuckme.network.provider.Provider;

public class MyAnswersFragment extends ResultsFragment {

    public static MyAnswersFragment newInstance() {
        MyAnswersFragment f = new MyAnswersFragment();
        return f;
    }

    @Override
    protected void init() {
        super.init();
        ((TextView) mNoResultsView).setText(R.string.error_message_no_answers);
    }

    @Override
    protected Provider loadProvider() {
        return new MyAnswersProvider();
    }
}