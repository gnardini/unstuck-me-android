package ar.com.wolox.unstuckme.fragment.results;

import android.widget.BaseAdapter;
import android.widget.TextView;

import ar.com.wolox.unstuckme.Configuration;
import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.adapter.ResultsAdapter;
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
    protected BaseAdapter loadAdapter() {
        return new ResultsAdapter(getActivity(), mList, Configuration.PRICE_UNBLOCK_MINE);
    }

    @Override
    protected Provider loadProvider() {
        return new MyQuestionsProvider();
    }
}