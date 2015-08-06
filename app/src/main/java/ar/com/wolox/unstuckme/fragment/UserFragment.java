package ar.com.wolox.unstuckme.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.util.ArrayList;
import java.util.List;

import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.UnstuckMeApplication;
import ar.com.wolox.unstuckme.model.User;
import ar.com.wolox.unstuckme.model.event.VotesSentEvent;
import ar.com.wolox.unstuckme.utils.AccessUtils;
import ar.com.wolox.unstuckme.utils.AnimationsHelper;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UserFragment extends Fragment {

    private static final String USER_STAT = "user_stat_";
    private static final String ID = "id";
    private static final String LEVEL = "Level %d";
    private static final String EXP = "%d/%d";

    private static final int STAT_CREDITS = 0;
    private static final int STAT_QUESTIONS_ASKED = 1;
    private static final int STAT_MY_ANSWERS = 2;
    private static final int STAT_MY_QUESTIONS_ANSWERS = 3;

    private static final int[] STATS_TITLES = {
            R.string.user_credits,
            R.string.user_questions_asked,
            R.string.user_questions_answered,
            R.string.user_my_questions_answered };

    private View mUserView;
    private TextView mLevel;
    private TextView mLevelExp;
    private RoundCornerProgressBar mLevelProgress;
    private List<UserStat> mUserStats;

    private View mProfile;
    private View mBack;
    private View mLoading;

    private User mUser;
    private int mExtraVotes;

    public static UserFragment newInstance() {
        UserFragment f = new UserFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user, container, false);
        setUi(v);
        init();
        populate();
        setListeners();
        return v;
    }

    private void setUi(View v) {
        mUserView = v.findViewById(R.id.user_data_container);
        mLevel = (TextView) v.findViewById(R.id.user_level);
        mLevelExp = (TextView) v.findViewById(R.id.user_level_exp);
        mLevelProgress = (RoundCornerProgressBar) v.findViewById(R.id.user_level_progress);

        mUserStats = new ArrayList<>(STATS_TITLES.length);
        for (int i = 0 ; i < STATS_TITLES.length ; i++) {
            int statId = getActivity().getResources().getIdentifier(USER_STAT + (i + 1), ID,
                    getActivity().getPackageName());
            UserStat userStat = new UserStat(v.findViewById(statId));
            userStat.mTitle.setText(STATS_TITLES[i]);
            mUserStats.add(i, userStat);
        }

        mBack = v.findViewById(R.id.toolbar_back);
        mProfile = v.findViewById(R.id.toolbar_user);
        mLoading = v.findViewById(R.id.user_loading);
    }

    private void init() {
        AnimationsHelper.startAnimation(getActivity(), mLoading, R.anim.rotate);
        UnstuckMeApplication.sUserService.getUserStats(new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                AccessUtils.updateUser(user);
                mUser = user;
                mLoading.clearAnimation();
                mLoading.setVisibility(View.GONE);
                mUserView.setVisibility(View.VISIBLE);
                setUserInfo();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), R.string.error_user_load_failed, Toast.LENGTH_SHORT);
                getActivity().finish();
            }
        });
    }

    private void setUserInfo() {
        mLevel.setText(String.format(LEVEL, mUser.getLevel()));
        mLevelExp.setText(String.format(EXP, mUser.getCurrentExp(), mUser.getExpToLevelUp()));
        mLevelProgress.setProgress(Math.max(1, mUser.getCurrentExp()));
        mLevelProgress.setMax(mUser.getExpToLevelUp());

        mUserStats.get(STAT_CREDITS).mValue.setText(String.valueOf(mUser.getCredits()));
        mUserStats.get(STAT_QUESTIONS_ASKED)
                .mValue.setText(String.valueOf(mUser.getQuestionsAsked()));
        mUserStats.get(STAT_MY_ANSWERS)
                .mValue.setText(String.valueOf(mUser.getAnsweredQuestions()));
        mUserStats.get(STAT_MY_QUESTIONS_ANSWERS)
                .mValue.setText(String.valueOf(mUser.getMyQuestionsAnswers()));
    }

    private void populate() {
        mProfile.setVisibility(View.GONE);
        mBack.setVisibility(View.VISIBLE);
    }

    private void setListeners() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }

    public void onEvent(VotesSentEvent event) {
        mExtraVotes += event.getVotes();
        mUserStats.get(STAT_MY_ANSWERS)
                .mValue.setText(String.valueOf(mExtraVotes + mUser.getAnsweredQuestions()));
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

    static class UserStat {

        TextView mTitle;
        TextView mValue;

        UserStat(View v) {
            mTitle = (TextView) v.findViewById(R.id.user_stat_name);
            mValue = (TextView) v.findViewById(R.id.user_stat_value);
        }
    }
}
