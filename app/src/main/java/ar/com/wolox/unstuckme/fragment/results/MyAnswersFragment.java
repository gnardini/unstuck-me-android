package ar.com.wolox.unstuckme.fragment.results;

import ar.com.wolox.unstuckme.network.provider.MyAnswersProvider;
import ar.com.wolox.unstuckme.network.provider.Provider;

public class MyAnswersFragment extends ResultsFragment {

    public static MyAnswersFragment newInstance() {
        MyAnswersFragment f = new MyAnswersFragment();
        return f;
    }

    @Override
    protected Provider loadProvider() {
        return new MyAnswersProvider();
    }
}
