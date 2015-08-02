package ar.com.wolox.unstuckme.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.UnstuckMeApplication;

public class AnimationsHelper {

    public static void popImage(View view, int duration) {
        Animation animation = AnimationUtils.loadAnimation(
                UnstuckMeApplication.getAppContext(),
                R.anim.abc_popup_enter);
        animation.setDuration(duration);
        view.startAnimation(animation);
    }

    public static void scaleDecelerate(View view) {
        Animation animation = AnimationUtils.loadAnimation(
                UnstuckMeApplication.getAppContext(),
                R.anim.scale_anticipate);
        view.startAnimation(animation);
    }

    public static Animation flyPlane(View view) {
        Animation animation = AnimationUtils.loadAnimation(
                UnstuckMeApplication.getAppContext(),
                R.anim.fly_plane);
        view.startAnimation(animation);
        return animation;
    }
}
