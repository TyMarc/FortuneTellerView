package com.mahinse.fortunetellerview;

/**
 * Created by mahinse on 3/1/2018.
 */

public interface PullListener {
    void onNewPullValue(final Direction direction, final float value);
    void onFinishedAnimation(final Direction direction);
}
