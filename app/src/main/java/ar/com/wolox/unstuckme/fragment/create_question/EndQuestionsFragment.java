package ar.com.wolox.unstuckme.fragment.create_question;

import android.app.Activity;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import ar.com.wolox.unstuckme.R;


public class EndQuestionsFragment extends Fragment {

    private ImageButton mOkButton;

    public static EndQuestionsFragment newInstance() {
        EndQuestionsFragment fragment = new EndQuestionsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_end_questions, container, false);
        setUi(v);
        setListeners();
        return v;
    }

    private void setUi(View v) {
        mOkButton = (ImageButton) v.findViewById(R.id.create_questions_end_button_ok);
    }

    private void setListeners() {
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.create_questions_container, CreateQuestionsFragment.newInstance())
                        .commit();
            }
        });
    }


}
