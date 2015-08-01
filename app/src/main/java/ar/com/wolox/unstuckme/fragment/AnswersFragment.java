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

public class AnswersFragment extends Fragment {

    private ViewPager mViewPager;
    private ResultsTabsAdapter mTabsAdapter;
    private TabLayout mTabLayout;

    public static AnswersFragment newInstance() {
        AnswersFragment f = new AnswersFragment();
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
        mTabLayout = (TabLayout) v.findViewById(R.id.answers_tabs);
    }

    private void init() {
        mTabsAdapter = new ResultsTabsAdapter(getActivity().getSupportFragmentManager(),
                getActivity());
        mViewPager.setAdapter(mTabsAdapter);
        mTabLayout.setTabsFromPagerAdapter(mTabsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
