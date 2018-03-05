package com.mahinse.fortunetellerview;

import android.animation.Animator;
import android.animation.ValueAnimator;

/**
 * Created by mahinse on 3/1/2018.
 */

public class PullAnimator {
    private static final String TAG = "PullAnimator";

    private static final int PULL_ANIMATION_DURATION = 100;
    private static final int PULL_BACK_ANIMATION_DURATION = 50;
    private static final float PULL_ANIMATION_FROM = 1f;
    private static final float PULL_ANIMATION_TO = 0.7f;

    private ValueAnimator valueAnimator;
    private ValueAnimator valueBackAnimator;

    protected PullAnimator(final PullListener pullListener, final Direction direction) {
        valueBackAnimator = ValueAnimator.ofFloat(PULL_ANIMATION_TO, PULL_ANIMATION_FROM).setDuration(PULL_BACK_ANIMATION_DURATION);
        valueBackAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                pullListener.onNewPullValue(direction, (float) animation.getAnimatedValue());
            }
        });

        valueAnimator = ValueAnimator.ofFloat(PULL_ANIMATION_FROM, PULL_ANIMATION_TO).setDuration(PULL_ANIMATION_DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                pullListener.onNewPullValue(direction, (float) animation.getAnimatedValue());
            }
        });

        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                valueBackAnimator.start();
                pullListener.onFinishedAnimation(direction);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    protected void startPullAnimation() {
        valueBackAnimator.cancel();
        valueAnimator.start();
    }
}
