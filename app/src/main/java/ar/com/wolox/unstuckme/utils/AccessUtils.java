package ar.com.wolox.unstuckme.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

import ar.com.wolox.unstuckme.UnstuckMeApplication;
import ar.com.wolox.unstuckme.model.User;

public class AccessUtils {

    private static final String ID = "ID";
    private static final String ANSWERED_QUESTIONS = "ANSWERED_QUESTIONS";
    private static final String MY_QUESTIONS_ASKED = "MY_QUESTIONS_ASKED";
    private static final String QUESTIONS_ASKED = "QUESTIONS_ASKED";
    private static final String CREDITS = "CREDITS";
    private static final String LEVEL = "LEVEL";
    private static final String CURRENT_EXP = "CURRENT_EXP";
    private static final String EXP_TO_LEVEL_UP = "EXP_TO_LEVEL_UP";

    private static User sUser;

    public static void updateUser(User user) {
        sUser = user;
        SharedPreferences.Editor editor = settings().edit();
        storeUser(editor);
        editor.apply();
    }

    public static void updateCredits() {
        SharedPreferences.Editor editor = settings().edit();
        editor.putInt(CREDITS, sUser.getCredits());
        editor.apply();
    }

    public static void updateCredits(User user) {
        sUser = user;
        SharedPreferences.Editor editor = settings().edit();
        editor.putInt(CREDITS, sUser.getCredits());
        editor.apply();
    }

    public static User getLoggedUser() {
        if (sUser != null) return sUser;
        loadUser();
        return sUser;
    }

    private static void storeUser(SharedPreferences.Editor editor) {
        editor.putInt(ID, sUser.getId());
        editor.putInt(ANSWERED_QUESTIONS, sUser.getAnsweredQuestions());
        editor.putInt(MY_QUESTIONS_ASKED, sUser.getMyQuestionsAnswers());
        editor.putInt(QUESTIONS_ASKED, sUser.getQuestionsAsked());
        editor.putInt(CREDITS, sUser.getCredits());
        editor.putInt(LEVEL, sUser.getLevel());
        editor.putInt(CURRENT_EXP, sUser.getCurrentExp());
        editor.putInt(EXP_TO_LEVEL_UP, sUser.getExpToLevelUp());
    }

    public static void loadUser() {
        SharedPreferences prefs = settings();
        sUser = new User(
                prefs.getInt(ID, 0),
                prefs.getInt(ANSWERED_QUESTIONS, 0),
                prefs.getInt(MY_QUESTIONS_ASKED, 0),
                prefs.getInt(QUESTIONS_ASKED, 0),
                prefs.getInt(CREDITS, 0),
                prefs.getInt(LEVEL, 1),
                prefs.getInt(CURRENT_EXP, 0),
                prefs.getInt(EXP_TO_LEVEL_UP, 0)
        );
    }

    private static SharedPreferences settings() {
        return getContext().getSharedPreferences(UnstuckMeApplication.SHARED_PREFERENCES,
                Activity.MODE_PRIVATE);
    }

    public static String getDeviceId() {
        return Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID)+"dfg";
    }

    private static Context getContext() {
        return UnstuckMeApplication.getAppContext();
    }
}
