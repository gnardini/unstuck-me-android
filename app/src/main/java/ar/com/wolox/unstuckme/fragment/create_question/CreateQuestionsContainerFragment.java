package ar.com.wolox.unstuckme.fragment.create_question;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.UnstuckMeApplication;

public class CreateQuestionsContainerFragment extends Fragment {

    public static CreateQuestionsContainerFragment newInstance() {
        CreateQuestionsContainerFragment fragment = new CreateQuestionsContainerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_questions_container, container, false);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.create_questions_container, CreateQuestionsFragment.newInstance())
                .commit();
    }
}
