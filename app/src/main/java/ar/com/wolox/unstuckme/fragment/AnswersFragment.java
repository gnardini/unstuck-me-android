package ar.com.wolox.unstuckme.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ar.com.wolox.unstuckme.R;

public class AnswersFragment extends Fragment {

    public static AnswersFragment newInstance() {
        AnswersFragment f = new AnswersFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_answers, container, false);

        return v;
    }
}
