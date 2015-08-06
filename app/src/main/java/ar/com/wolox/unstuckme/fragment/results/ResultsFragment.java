package ar.com.wolox.unstuckme.fragment.results;

import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.fragment.endless.EndlessScrollListFragment;
import ar.com.wolox.unstuckme.model.Question;

public abstract class ResultsFragment extends EndlessScrollListFragment<Question> {

    @Override
    protected int layout() {
        return R.layout.fragment_results;
    }
}
