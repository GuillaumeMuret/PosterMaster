package com.murey.poster.postermaster.utils;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.murey.poster.postermaster.R;
import com.murey.poster.postermaster.model.constant.Constant;

public class ViewUtils {

    /**
     * Process called to display the under item
     * @param underLayout
     */
    public static void makeLayoutVisible(Context context, View underLayout){
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in);
            underLayout.setAnimation(animation);
            underLayout.animate();
            underLayout.setVisibility(View.VISIBLE);
            animation.start();
    }

    /**
     * Process called when under item gone
     * @param underLayout
     */
    public static void makeLayoutGone(Context context, final View underLayout){
        Animation animation= AnimationUtils.loadAnimation(context, R.anim.slide_out);
        animation.setFillAfter(true);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //Extra work goes here
                underLayout.setVisibility(View.GONE);
            }
        }, Constant.TIME_ANIMATION_FADE_OUT);
        underLayout.setAnimation(animation);
        underLayout.animate();
        underLayout.startAnimation(animation);
    }

    public static void makeRotate90Clockwise(Context context, final View underLayout){
        Animation animation= AnimationUtils.loadAnimation(context, R.anim.rotate_90_clockwise);
        animation.setFillAfter(true);
        underLayout.setAnimation(animation);
        underLayout.animate();
        underLayout.startAnimation(animation);
    }

    public static void makeRotate90Anticlockwise(Context context, final View underLayout){
        Animation animation= AnimationUtils.loadAnimation(context, R.anim.rotate_90_anticlockwise);
        animation.setFillAfter(true);
        underLayout.setAnimation(animation);
        underLayout.animate();
        underLayout.startAnimation(animation);
    }
}
