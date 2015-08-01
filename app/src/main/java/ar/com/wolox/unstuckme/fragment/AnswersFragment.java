package ar.com.wolox.unstuckme.fragment;

import android.widget.BaseAdapter;

import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.adapter.AnswersAdapter;
import ar.com.wolox.unstuckme.fragment.endless.EndlessScrollListFragment;
import ar.com.wolox.unstuckme.model.Question;
import ar.com.wolox.unstuckme.network.provider.MyAnswersProvider;
import ar.com.wolox.unstuckme.network.provider.Provider;

public class AnswersFragment extends EndlessScrollListFragment<Question> {

    public static AnswersFragment newInstance() {
        AnswersFragment f = new AnswersFragment();
        return f;
    }

    @Override
    protected int layout() {
        return R.layout.fragment_answers;
    }

    @Override
    protected Provider loadProvider() {
        return new MyAnswersProvider();
    }

    @Override
    protected BaseAdapter loadAdapter() {
        return new AnswersAdapter(getActivity(), mList);
    }
}
