package ar.com.wolox.unstuckme.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.fragment.results.MyAnswersFragment;
import ar.com.wolox.unstuckme.fragment.results.MyQuestionsFragment;

public class ResultsTabsAdapter extends FragmentStatePagerAdapter {

    private static final int TABS_COUNT = 2;
    public static final int MY_ANSWERS = 0;
    public static final int MY_QUESTIONS = 1;

    private static final int[] mTabTitles = {R.string.my_answers, R.string.my_questions};

    private Context mContext;
    private MyAnswersFragment mMyAnswersFragment;
    private MyQuestionsFragment mMyQuestionsFragment;

    public ResultsTabsAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
        mMyAnswersFragment = MyAnswersFragment.newInstance();
        mMyQuestionsFragment = MyQuestionsFragment.newInstance();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case MY_ANSWERS:
                return mMyAnswersFragment;
            case MY_QUESTIONS:
                return mMyQuestionsFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return TABS_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getString(mTabTitles[position]);
    }
}