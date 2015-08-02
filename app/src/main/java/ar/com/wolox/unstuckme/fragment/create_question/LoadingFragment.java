package ar.com.wolox.unstuckme.fragment.create_question;

import android.app.Activity;
import android.app.PendingIntent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.model.Question;
import ar.com.wolox.unstuckme.utils.QuestionBuilder;


public class LoadingFragment extends Fragment implements FinishLoadingListener{

    Question questionToShare;

    @Override
    public void onFinish() {
        makeTransition();
    }

    public static LoadingFragment newInstance() {
        LoadingFragment fragment = new LoadingFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_loading, container, false);
        setUi(v);
        QuestionBuilder.setListener(this);
        setListeners();
        return v;
    }

    private void setUi(View v) {
        Animation rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        v.findViewById(R.id.create_questions_spinner).startAnimation(rotation);
    }

    private void setListeners() {


    }

    public void makeTransition() {
        FragmentManager fm = getFragmentManager();
//        fm.popBackStack();
            fm.beginTransaction()
                .replace(R.id.create_questions_container, ShareQuestionFragment.newInstance())
                .commit();


    }


    
}
