package ar.com.wolox.unstuckme.utils;

import com.parse.ParseInstallation;

public class PushNotificationUtils {

    private static final String DEVICE_TOKEN = "device_token";

    public static void subscribe() {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put(DEVICE_TOKEN, AccessUtils.getDeviceId());
        installation.saveInBackground();
    }

    public static void unsubscribe() {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put(DEVICE_TOKEN, null);
        installation.saveInBackground();
    }
}
