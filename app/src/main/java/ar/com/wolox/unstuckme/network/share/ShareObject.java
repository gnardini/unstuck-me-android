package ar.com.wolox.unstuckme.network.share;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import ar.com.wolox.unstuckme.Configuration;
import ar.com.wolox.unstuckme.R;

public class ShareObject {

    private static final String PLAIN_TEXT = "text/plain";

    private Context mContext;
    private int mQuestionId;

    public ShareObject(Context context, int questionId) {
        mContext = context;
        mQuestionId = questionId;
    }

    public void share() {
        Toast.makeText(mContext, R.string.share_preparing, Toast.LENGTH_LONG).show();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, Configuration.API_ENDPOINT
                + Configuration.ADD_QUERY
                + mQuestionId);
        sendIntent.setType(PLAIN_TEXT);
        mContext.startActivity(sendIntent);
    }
}
