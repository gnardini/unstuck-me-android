package ar.com.wolox.unstuckme.network.notification;

import android.content.Context;
import android.content.Intent;

import com.parse.ParseAnalytics;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import ar.com.wolox.unstuckme.Configuration;
import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.activity.MainActivity;

public class PushReceiver extends ParsePushBroadcastReceiver {

    private static final int APP_PUSH_ID = 17523;

    public static final String QUESTION_ID = "question_id";
    public static final String TYPE = "type";
    public static final String LIMIT = "limit";
    public static final String LEVELUP = "levelup";

    private static final String PARSE_DATA_PARAMETER = "com.parse.Data";
    private static final String DATA = "data";
    private static final String ALERT = "alert";
    private static final String TITLE = "title";

    private int mNotificationsCount;

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        ParseAnalytics.trackAppOpenedInBackground(intent);
        mNotificationsCount = 0;
        int questionId;
        String type;
        try {
            JSONObject data = new JSONObject(intent.getStringExtra(PARSE_DATA_PARAMETER));
            questionId = data.getInt(QUESTION_ID);
            type = data.getString(DATA);
        } catch (JSONException e) {
            questionId = Configuration.QUESTION_ID_ERROR;
            type = null;
        }
        if (type != null) {
            if (type.equals(LIMIT)) {
                Intent newIntent = new Intent(context, MainActivity.class);
                newIntent.putExtra(QUESTION_ID, questionId);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(newIntent);
            } else if (type.equals(LEVELUP)) {
                // TODO
            }
        }
    }

    @Override
    public void onReceive(Context contextIntent, Intent intent) {
        mNotificationsCount++;
        try {
            JSONObject pushData = new JSONObject(intent.getStringExtra(PARSE_DATA_PARAMETER));
            int questionId = pushData
                    .getJSONObject(DATA)
                    .getInt(QUESTION_ID);
            String type = pushData
                    .getJSONObject(DATA)
                    .getString(TYPE);
            pushData.put(QUESTION_ID, questionId);
            pushData.put(DATA, type);
            pushData.put(ALERT, contextIntent.getString(R.string.push_notification_text));
            pushData.put(TITLE, contextIntent.getString(R.string.app_name));
            intent.putExtra(PARSE_DATA_PARAMETER, pushData.toString());
            super.onReceive(contextIntent, intent);
        } catch (JSONException e) {
            return;
        }
    }
}