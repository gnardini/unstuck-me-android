package ar.com.wolox.unstuckme.utils;

import android.content.Context;
import android.provider.Settings;

import ar.com.wolox.unstuckme.UnstuckMeApplication;

public class AccessUtils {

    public static String getDeviceId() {
        return "gfdgs" + Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    private static Context getContext() {
        return UnstuckMeApplication.getAppContext();
    }
}
