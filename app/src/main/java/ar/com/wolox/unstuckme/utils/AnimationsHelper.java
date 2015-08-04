package ar.com.wolox.unstuckme.utils;

import android.content.Context;
import android.support.annotation.AnimRes;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.UnstuckMeApplication;

public class AnimationsHelper {

    public static Animation startAnimation(Context context, View view, @AnimRes int animId) {
        Animation animation = AnimationUtils.loadAnimation(context, animId);
        view.startAnimation(animation);
        return animation;
    }

    public static Animation startAnimation(Context context, View view,
                                           @AnimRes int animId, int duration) {
        Animation animation = AnimationUtils.loadAnimation(context, animId);
        animation.setDuration(duration);
        view.startAnimation(animation);
        return animation;
    }
}
