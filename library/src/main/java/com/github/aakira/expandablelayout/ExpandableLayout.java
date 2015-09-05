package com.github.aakira.expandablelayout;

import android.animation.TimeInterpolator;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface ExpandableLayout {

    /**
     * Duration of expand animation
     */
    int DEFAULT_DURATION = 300;
    /**
     * Visibility of the layout when the layout attaches
     */
    boolean DEFAULT_VISIBILITY = false;
    /**
     * Orientation of child views
     */
    int HORIZONTAL = 0;
    /**
     * Orientation of child views
     */
    int VERTICAL = 1;

    /**
     * Orientation of layout
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({HORIZONTAL, VERTICAL})
    @interface Orientation {
    }

    /**
     * Starts animation the state of the view to the inverse of its current state
     */
    void toggle();

    /**
     * Starts expand animation
     */
    void expand();

    /**
     * Starts collapse animation
     */
    void collapse();

    /**
     * Sets the expandable layout listener
     *
     * @param listener ExpandableLayoutListener
     */
    void setListener(@NonNull final ExpandableLayoutListener listener);

    /**
     * Sets the length of the animation.
     * The default duration is 300 milliseconds.
     *
     * @param duration
     */
    void setDuration(@NonNull final int duration);

    /**
     * Sets state of expanse at first visibility
     *
     * @param defaultVisibility
     */
    void setDefaultVisibility(@NonNull final boolean defaultVisibility);

    /**
     * The time interpolator used in calculating the elapsed fraction of this animation. The
     * interpolator determines whether the animation runs with linear or non-linear motion,
     * such as acceleration and deceleration.
     * The default value is  {@link android.view.animation.AccelerateDecelerateInterpolator}
     *
     * @param interpolator
     */
    void setInterpolator(@NonNull final TimeInterpolator interpolator);
}