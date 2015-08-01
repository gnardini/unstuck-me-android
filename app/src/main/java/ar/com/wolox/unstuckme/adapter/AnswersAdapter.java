package ar.com.wolox.unstuckme.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.model.Option;
import ar.com.wolox.unstuckme.model.Question;

public class AnswersAdapter extends BaseAdapter {

    private static final int MAX_IMAGES = 4;
    private static final String PICTURE_ELEMENT = "adapter_answer_element_";
    private static final String ID = "id";

    private List<Question> mList;
    private Context mContext;

    public AnswersAdapter(Context context, List<Question> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder v;
        if (view == null) {
            view = inflater.inflate(R.layout.adapter_answers, parent, false);
            if (view == null) return null;
            v = newViewHolder(view);
            view.setTag(v);
        } else {
            v = (ViewHolder) view.getTag();
        }
        Question question = mList.get(position);
        populate(question, v);
        setListeners(question, v, view);
        return view;
    }

    private void populate(Question question, ViewHolder v) {
        List<Option> options = question.getOptions();
        int winner = question.getWinnerIndex();
        for (int i = 0 ; i < MAX_IMAGES ; i++) {
            if ( i < options.size()) {
                v.mPictures[i].mRoot.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(options.get(i).getOption())
                        .placeholder(null)
                        .into(v.mPictures[i].mImage);
            } else {
                v.mPictures[i].mRoot.setVisibility(View.GONE);
            }
            v.mPictures[i].mImageHighlight.setVisibility((winner == i)
                    ? View.VISIBLE : View.GONE);
        }
    }

    private void setListeners(final Question question, final ViewHolder v, final View view) {
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                return false;
            }
        });
    }

    static class ViewHolder {
        PictureElement[] mPictures;
    }

    public ViewHolder newViewHolder(View view) {
        ViewHolder v = new ViewHolder();
        v.mPictures = new PictureElement[MAX_IMAGES];
        for (int i = 0 ; i < MAX_IMAGES ; i++) {
            int elementId = mContext.getResources().getIdentifier(PICTURE_ELEMENT + (i + 1), ID,
                    mContext.getPackageName());
            View elementView = view.findViewById(elementId);
            PictureElement pictureElement = new PictureElement(elementView);
            v.mPictures[i] = pictureElement;
        }
        return v;
    }

    static class PictureElement {

        View mRoot;
        ImageView mImage;
        TextView mPercentage;
        View mImageHighlight;

        public PictureElement(View view) {
            mRoot = view;
            mImage = (ImageView) view.findViewById(R.id.adapter_answers_image);
            mPercentage = (TextView) view.findViewById(R.id.adapter_answers_percentage);
            mImageHighlight = view.findViewById(R.id.adapter_answers_highlight);
        }
    }
}