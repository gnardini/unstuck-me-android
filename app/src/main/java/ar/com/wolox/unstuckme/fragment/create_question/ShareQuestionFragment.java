package ar.com.wolox.unstuckme.fragment.create_question;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ar.com.wolox.unstuckme.R;

public class ShareQuestionFragment extends Fragment {

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
        setListeners();
        return v;
    }

    private void setUi(View v) {

    }

    private void setListeners() {
    }

}
