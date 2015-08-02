package ar.com.wolox.unstuckme.fragment.create_question;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.model.Question;
import ar.com.wolox.unstuckme.network.share.ShareObject;
import ar.com.wolox.unstuckme.utils.QuestionBuilder;

public class ShareQuestionFragment extends Fragment {

    private Question questionToShare;
    private ImageView mShareQuestionButton;
    private TextView mNoThanksTextView;

    public static ShareQuestionFragment newInstance() {
        ShareQuestionFragment fragment = new ShareQuestionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_share_question, container, false);
        setUi(v);
        init();
        setListeners();
        return v;
    }

    private void init() {
        questionToShare = QuestionBuilder.getQuestionReady();
    }

    private void setUi(View v) {
        mShareQuestionButton = (ImageView) v.findViewById(R.id.create_questions_share_goto_app);
        mNoThanksTextView = (TextView) v.findViewById(R.id.create_questions_share_no_thanks);
    }

    private void setListeners() {
        mShareQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareObject(getActivity(), questionToShare.getId()).share();
                makeTransition();
            }
        });

        mNoThanksTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeTransition();
            }
        });
    }

    private void makeTransition() {
        FragmentManager fm = getFragmentManager();
                fm.beginTransaction()
                .replace(R.id.create_questions_container, EndQuestionsFragment.newInstance())
                .commit();

    }
}
