package ar.com.wolox.unstuckme.fragment.results;

import ar.com.wolox.unstuckme.network.provider.MyQuestionsProvider;
import ar.com.wolox.unstuckme.network.provider.Provider;

public class MyQuestionsFragment extends ResultsFragment {

    public static MyQuestionsFragment newInstance() {
        MyQuestionsFragment f = new MyQuestionsFragment();
        return f;
    }

    @Override
    protected Provider loadProvider() {
        return new MyQuestionsProvider();
    }
}
