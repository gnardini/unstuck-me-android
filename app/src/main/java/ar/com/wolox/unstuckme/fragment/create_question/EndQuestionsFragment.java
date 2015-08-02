package ar.com.wolox.unstuckme.fragment.create_question;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import ar.com.wolox.unstuckme.R;


public class EndQuestionsFragment extends Fragment {

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
    }

    private void setListeners() {
    }


}
