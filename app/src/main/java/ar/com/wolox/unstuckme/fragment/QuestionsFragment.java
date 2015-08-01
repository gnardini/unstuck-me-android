package ar.com.wolox.unstuckme.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import ar.com.wolox.unstuckme.R;

public class QuestionsFragment extends Fragment {

    private List<ImageView> mAnswerImages = new ArrayList<>();
    private List<ImageView> mAnswerImagesTick = new ArrayList<>();

    private View.OnClickListener mImageAnswerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int tickPos = mAnswerImages.indexOf(view);
            mAnswerImagesTick.get(tickPos).setVisibility(View.VISIBLE);
        }
    };

    public static QuestionsFragment newInstance() {
        QuestionsFragment f = new QuestionsFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_questions, container, false);

        setUi(v);
        init();
        setListeners();
        return v;
    }


    private void setUi(View view) {
        mAnswerImages.add( (ImageView) view.findViewById(R.id.questions_imageview_answer_1));
        mAnswerImages.add( (ImageView) view.findViewById(R.id.questions_imageview_answer_2));
        mAnswerImages.add( (ImageView) view.findViewById(R.id.questions_imageview_answer_3));
        mAnswerImages.add( (ImageView) view.findViewById(R.id.questions_imageview_answer_4));

        mAnswerImagesTick.add( (ImageView) view.findViewById(R.id.questions_imageview_answer_tick_1));
        mAnswerImagesTick.add( (ImageView) view.findViewById(R.id.questions_imageview_answer_tick_2));
        mAnswerImagesTick.add( (ImageView) view.findViewById(R.id.questions_imageview_answer_tick_3));
        mAnswerImagesTick.add( (ImageView) view.findViewById(R.id.questions_imageview_answer_tick_4));
    }

    private void init() {

    }

    private void setListeners() {
        for (View view : mAnswerImages) {
            view.setOnClickListener(mImageAnswerClickListener);
        }
    }
}
