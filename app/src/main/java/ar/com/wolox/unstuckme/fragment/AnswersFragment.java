package ar.com.wolox.unstuckme.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.adapter.ResultsTabsAdapter;
import ar.com.wolox.unstuckme.network.notification.PushReceiver;
import ar.com.wolox.unstuckme.view.SlidingTabLayout;

public class AnswersFragment extends Fragment {

    private ViewPager mViewPager;
    private ResultsTabsAdapter mTabsAdapter;
    private SlidingTabLayout mSlidingTabs;

    public static AnswersFragment newInstance(int questionId) {
        AnswersFragment f = new AnswersFragment();
        Bundle args = new Bundle();
        args.putInt(PushReceiver.QUESTION_ID, questionId);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_answers, container, false);
        setUi(v);
        init();
        return v;
    }

    private void setUi(View v) {
        mViewPager = (ViewPager) v.findViewById(R.id.answers_pager);
        mSlidingTabs = (SlidingTabLayout) v.findViewById(R.id.answers_tabs);
    }

    private void init() {
        mTabsAdapter = new ResultsTabsAdapter(getActivity().getSupportFragmentManager(),
                getActivity());
        mViewPager.setAdapter(mTabsAdapter);
        mSlidingTabs.setViewPager(mViewPager);
        mSlidingTabs.setDistributeEvenly(true);

        Bundle args = getArguments();
        if (args != null && args.containsKey(PushReceiver.QUESTION_ID)) {
            int questionId = args.getInt(PushReceiver.QUESTION_ID);
            if (questionId != PushReceiver.QUESTION_ID_ERROR) {
                mViewPager.setCurrentItem(ResultsTabsAdapter.MY_QUESTIONS);
            }
        }
    }
}