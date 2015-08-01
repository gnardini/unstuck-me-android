package ar.com.wolox.unstuckme.fragment.results;

import android.widget.BaseAdapter;

import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.adapter.ResultsAdapter;
import ar.com.wolox.unstuckme.fragment.endless.EndlessScrollListFragment;
import ar.com.wolox.unstuckme.model.Question;
import ar.com.wolox.unstuckme.network.provider.MyAnswersProvider;
import ar.com.wolox.unstuckme.network.provider.Provider;

public abstract class ResultsFragment extends EndlessScrollListFragment<Question> {

    @Override
    protected int layout() {
        return R.layout.fragment_results;
    }

    @Override
    protected BaseAdapter loadAdapter() {
        return new ResultsAdapter(getActivity(), mList);
    }
}
