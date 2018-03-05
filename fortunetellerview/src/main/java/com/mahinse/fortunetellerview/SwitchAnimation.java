package com.mahinse.fortunetellerview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;

/**
 * Created by mahinse on 3/5/2018.
 */

public class SwitchAnimation {
    private static final String TAG = "SwitchAnimation";

    private static final float SCALE_MAX = 0.5f;
    private static final int SCALE_DOWN_DURATION = 100;
    private static final int SCALE_UP_DURATION = 100;
    private static final int ROTATE_DURATION = 200;

    public ValueAnimator scaleDownAnimator;
    public ValueAnimator scaleUpAnimator;
    public ValueAnimator rotateAnimator;
    private SwitchListener listener;

    public SwitchAnimation(final View view) {
        scaleDownAnimator = ValueAnimator.ofFloat(1f, SCALE_MAX).setDuration(SCALE_DOWN_DURATION);
        scaleDownAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                view.setScaleX(value);
                view.setScaleY(value);
                view.invalidate();
            }
        });

        scaleDownAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(listener != null) {
                    listener.onChangeChoices();
                }
                scaleUpAnimator.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        scaleUpAnimator = ValueAnimator.ofFloat(SCALE_MAX, 1f).setDuration(SCALE_UP_DURATION);
        scaleUpAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                view.setScaleX(value);
                view.setScaleY(value);
                view.invalidate();
            }
        });

        rotateAnimator = ValueAnimator.ofFloat(0f, 360f).setDuration(ROTATE_DURATION);
        rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                view.setRotation(value);
                view.invalidate();
            }
        });
    }

    public void startAnimation(final SwitchListener listener) {
        this.listener = listener;
        scaleDownAnimator.start();
        rotateAnimator.start();
    }
}
