package ar.com.wolox.unstuckme.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.DraweeView;

import java.util.List;

import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.UnstuckMeApplication;
import ar.com.wolox.unstuckme.activity.FullscreenImageActivity;
import ar.com.wolox.unstuckme.activity.MainActivity;
import ar.com.wolox.unstuckme.model.Option;
import ar.com.wolox.unstuckme.model.Question;
import ar.com.wolox.unstuckme.network.share.ShareObject;
import ar.com.wolox.unstuckme.utils.AccessUtils;
import ar.com.wolox.unstuckme.utils.CloudinaryUtils;
import ar.com.wolox.unstuckme.utils.ToastUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ResultsAdapter extends BaseAdapter {

    private static final String TOTAL_VOTES = "Total Votes: %d";
    private static final String PRICE = "%d credits";
    private static final int MAX_IMAGES = 4;
    private static final String PICTURE_ELEMENT = "adapter_answer_element_";
    private static final String ID = "id";

    private List<Question> mList;
    private Context mContext;
    private int mPrice;
    private View mLongClicked;

    public ResultsAdapter(Context context, List<Question> list, int price) {
        mContext = context;
        mList = list;
        mPrice = price;
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
            view = inflater.inflate(R.layout.adapter_results, parent, false);
            if (view == null) return null;
            v = newViewHolder(view);
            view.setTag(v);
        } else {
            v = (ViewHolder) view.getTag();
        }
        Question question = mList.get(position);
        populate(question, v);
        setRowListeners(question, v, view);
        return view;
    }

    private void populate(final Question question, final ViewHolder v) {
        List<Option> options = question.getOptions();
        final int winner = question.getWinnerIndex();
        for (int i = 0 ; i < MAX_IMAGES ; i++) {
            if ( i < options.size()) {
                final String imageUri = options.get(i).getImageUrl();
                final boolean isWinner = (i == winner);
                v.mPictures[i].mRoot.setVisibility(View.VISIBLE);
                v.mPictures[i].mImage.setImageURI(
                        Uri.parse(CloudinaryUtils.getReducedImage(imageUri)));
                v.mPictures[i].mImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(mContext, FullscreenImageActivity.class);
                        i.putExtra(FullscreenImageActivity.EXT_LOW_RES_URI,
                                CloudinaryUtils.getReducedImage(imageUri));
                        i.putExtra(FullscreenImageActivity.EXT_FULL_RES_URI,
                                CloudinaryUtils.getFullScreenImage(imageUri));
                        i.putExtra(FullscreenImageActivity.EXT_IS_WINNER, isWinner);
                        mContext.startActivity(i);
                    }
                });
                v.mPictures[i].mImage.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if (!question.isUnlocked()) return false;
                        if (mLongClicked != null) mLongClicked.setVisibility(View.GONE);
                        mLongClicked = v.mHighlight;
                        v.mHighlight.setVisibility(View.VISIBLE);
                        v.mTotalVotes.setText(String.format(TOTAL_VOTES, question.getTotalVotes()));
                        return true;
                    }
                });
            } else {
                v.mPictures[i].mRoot.setVisibility(View.GONE);
            }
            v.mPictures[i].mImageHighlight.setVisibility(
                    (question.isUnlocked()
                    && winner == i
                    && options.get(winner).getVotes() > 1)
                    ? View.VISIBLE : View.GONE);
        }
        v.mHighlight.setVisibility(View.GONE);
        v.mLocked.setVisibility(question.isUnlocked() ? View.GONE : View.VISIBLE);
        if (!question.isUnlocked()) {
            v.mPrice.setText(String.format(PRICE, mPrice));
        }
    }

    private void setRowListeners(final Question question, final ViewHolder v, final View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLongClicked != null) mLongClicked.setVisibility(View.GONE);
            }
        });
        v.mHighlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v.mHighlight.setVisibility(View.GONE);
            }
        });
        v.mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareObject(mContext, question.getId()).share();
            }
        });
        v.mLocked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AccessUtils.getLoggedUser().getCredits() >= mPrice) {
                    showConfirmationDialog(question, v);
                } else {
                    ToastUtils.showToast(mContext, R.string.results_no_credits);
                }
            }
        });
    }

    private void showConfirmationDialog(final Question question, final ViewHolder v) {
        new AlertDialog.Builder(mContext)
                .setTitle(R.string.app_name)
                .setIcon(R.drawable.selector_tab_questions)
                .setMessage(mContext.getString(R.string.results_confirm_unlock, mPrice))
                .setPositiveButton(R.string.results_unlock_yes,
                        new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UnstuckMeApplication.sQuestionsService.unlockQuestion(
                                question.getId(), "",
                                new Callback<Void>() {
                                    @Override
                                    public void success(Void aVoid, Response response) {
                                        v.mLocked.setVisibility(View.GONE);
                                        updateCredits();
                                        question.unlock();
                                        populate(question, v);
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        ToastUtils.showToast(mContext,
                                                R.string.results_unlock_fail);
                                    }
                                });
                    }
                })
                .setNegativeButton(R.string.results_unlock_no, null)
                .show();
    }

    private void updateCredits() {
        ((MainActivity) mContext).addCredits(-mPrice);
    }

    static class ViewHolder {

        PictureElement[] mPictures;
        TextView mTotalVotes;
        View mHighlight;
        View mShare;
        View mLocked;
        TextView mPrice;
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
        v.mTotalVotes = (TextView) view.findViewById(R.id.results_total_votes);
        v.mHighlight = view.findViewById(R.id.results_highlight);
        v.mShare = view.findViewById(R.id.results_share);
        v.mLocked = view.findViewById(R.id.results_locked);
        v.mPrice = (TextView) view.findViewById(R.id.results_unlock_price);
        return v;
    }

    static class PictureElement {

        View mRoot;
        DraweeView mImage;
        View mImageHighlight;

        public PictureElement(View view) {
            mRoot = view;
            mImage = (DraweeView) view.findViewById(R.id.adapter_results_image);
            mImageHighlight = view.findViewById(R.id.adapter_results_highlight);
        }
    }
}