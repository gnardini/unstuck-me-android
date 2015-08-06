package ar.com.wolox.unstuckme.fragment.results;

import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import ar.com.wolox.unstuckme.Configuration;
import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.adapter.ResultsAdapter;
import ar.com.wolox.unstuckme.model.event.QuestionAnsweredEvent;
import ar.com.wolox.unstuckme.network.provider.MyAnswersProvider;
import ar.com.wolox.unstuckme.network.provider.Provider;
import de.greenrobot.event.EventBus;

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
    protected BaseAdapter loadAdapter() {
        return new ResultsAdapter(getActivity(), mList, Configuration.PRICE_UNBLOCK_OTHER);
    }

    @Override
    protected Provider loadProvider() {
        return new MyAnswersProvider();
    }

    public void onEvent(QuestionAnsweredEvent event) {
        mList.add(0, event.getQuestion());
        if (mList.size() > ELEMENTS_PER_PAGE) mList.remove(mList.size() - 1);
        mAdapter.notifyDataSetChanged();
        mNoResultsView.setVisibility(View.GONE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}