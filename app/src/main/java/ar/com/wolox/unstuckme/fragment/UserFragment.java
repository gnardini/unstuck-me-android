package ar.com.wolox.unstuckme.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.UnstuckMeApplication;
import ar.com.wolox.unstuckme.model.User;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UserFragment extends Fragment {

    private static final String LEVEL = "Level %d";
    private static final String EXP = "%d/%d";

    private User mUser;
    private View mShare;
    private View mProfile;
    private View mBack;
    private TextView mLevel;
    private RoundCornerProgressBar mLevelProgress;
    private TextView mLevelExp;
    private TextView mAnswersCount;
    private TextView mMyQuestionsAnswered;
    private TextView mQuestionsAsked;

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
        mBack = v.findViewById(R.id.toolbar_back);
        mShare = v.findViewById(R.id.toolbar_share);
        mProfile = v.findViewById(R.id.toolbar_user);
        mLevel = (TextView) v.findViewById(R.id.user_level);
        mLevelProgress = (RoundCornerProgressBar) v.findViewById(R.id.user_level_progress);
        mLevelExp = (TextView) v.findViewById(R.id.user_level_exp);
        mAnswersCount = (TextView) v.findViewById(R.id.user_questions_answered_count);
        mMyQuestionsAnswered = (TextView) v.findViewById(R.id.user_my_questions_answered_count);
        mQuestionsAsked = (TextView) v.findViewById(R.id.user_questions_asked);
    }

    private void init() {
        UnstuckMeApplication.sUserService.getUserStats(new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                mUser = user;
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
        mLevelProgress.setProgress(Math.max(1, mUser.getCurrentExp()));
        mLevelProgress.setMax(mUser.getExpToLevelUp());
        mLevelExp.setText(String.format(EXP, mUser.getCurrentExp(), mUser.getExpToLevelUp()));
        mAnswersCount.setText(String.valueOf(mUser.getAnsweredQuestions()));
        mMyQuestionsAnswered.setText(String.valueOf(mUser.getMyQuestionsAnswers()));
        mQuestionsAsked.setText(String.valueOf(mUser.getQuestionsAsked()));
    }

    private void populate() {
        mShare.setVisibility(View.GONE);
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
}
