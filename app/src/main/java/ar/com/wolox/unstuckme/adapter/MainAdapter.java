package ar.com.wolox.unstuckme.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ar.com.wolox.unstuckme.fragment.AnswersFragment;
import ar.com.wolox.unstuckme.fragment.QuestionsFragment;
import ar.com.wolox.unstuckme.fragment.create_question.CreateQuestionsContainerFragment;
public class MainAdapter extends FragmentStatePagerAdapter {

    public static final int TABS_COUNT = 3;
    private static final int ANSWERS = 0;
    private static final int QUESTIONS = 1;
    private static final int CREATE_QUESTIONS = 2;

    private Context mContext;
    private AnswersFragment mAnswersFragment;
    private QuestionsFragment mQuestionsFragment;
    private CreateQuestionsContainerFragment mCreateQuestionsContainerFragment;

    public MainAdapter(FragmentManager fm, Context context,
                       int pushQuestionId, int shareQuestionId) {
        super(fm);
        mContext = context;
        mAnswersFragment = AnswersFragment.newInstance(pushQuestionId);
        mQuestionsFragment = QuestionsFragment.newInstance(shareQuestionId);
        mCreateQuestionsContainerFragment = CreateQuestionsContainerFragment.newInstance();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case ANSWERS:
                return mAnswersFragment;
            case QUESTIONS:
                return mQuestionsFragment;
            case CREATE_QUESTIONS:
                return mCreateQuestionsContainerFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return TABS_COUNT;
    }
}