package ar.com.wolox.unstuckme.fragment.create_question;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.utils.QuestionBuilder;

public class PrivacyQuestionsFragment extends Fragment {

    private ImageButton mPrivacyOnButton;
    private ImageButton mPrivacyOffButton;

    public static PrivacyQuestionsFragment newInstance() {
        PrivacyQuestionsFragment fragment = new PrivacyQuestionsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_privacy, container, false);
        setUi(v);
        setListeners();
        return v;
    }

    private void setUi(View v) {
        mPrivacyOnButton = (ImageButton) v.findViewById(R.id.create_questions_privacy_on);
        mPrivacyOffButton = (ImageButton) v.findViewById(R.id.create_questions_privacy_off);
    }

    private void setListeners() {
        mPrivacyOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuestionBuilder.putPrivacy(true);
                makeTransition();
            }
        });

        mPrivacyOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuestionBuilder.putPrivacy(false);
                makeTransition();
            }
        });
    }

    private void makeTransition() {
        QuestionBuilder.handleImageLoading();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.create_questions_container, EndQuestionsFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }


}
