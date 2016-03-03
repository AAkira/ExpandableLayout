package com.github.aakira.expandablelayout;

import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class ExpandableRelativeLayout extends RelativeLayout implements ExpandableLayout {

    private ExpandableLayoutDelegate mDelegate;

    public ExpandableRelativeLayout(final Context context) {
        this(context, null);
    }

    public ExpandableRelativeLayout(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableRelativeLayout(final Context context, final AttributeSet attrs,
                                    final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExpandableRelativeLayout(final Context context, final AttributeSet attrs,
                                    final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        mDelegate = new ExpandableLayoutDelegate(this);
        mDelegate.init(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mDelegate.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return mDelegate.onSaveInstanceState(super.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(final Parcelable state) {
        if (!(state instanceof ExpandableSavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        final ExpandableSavedState ss = (ExpandableSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mDelegate.onRestoreInstanceState(ss);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setListener(@NonNull ExpandableLayoutListener listener) {
        mDelegate.setListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toggle() {
        mDelegate.toggle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void expand() {
        mDelegate.expand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void collapse() {
        mDelegate.collapse();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initLayout(final boolean isMaintain) {
        mDelegate.initLayout(isMaintain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDuration(final int duration) {
        mDelegate.setDuration(duration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setExpanded(boolean expanded) {
        mDelegate.setExpanded(expanded);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExpanded() {
        return mDelegate.isExpanded();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInterpolator(@NonNull final TimeInterpolator interpolator) {
        mDelegate.setInterpolator(interpolator);
    }

    /**
     * @param position
     * @see #move(int, long, TimeInterpolator)
     */
    public void move(int position) {
        mDelegate.move(position);
    }

    /**
     * Moves to position
     *
     * @param position
     * @param duration
     * @param interpolator
     */
    public void move(int position, long duration, TimeInterpolator interpolator) {
        mDelegate.move(position, duration, interpolator);
    }

    /**
     * @param index child view index
     * @see #moveChild(int, long, TimeInterpolator)
     */
    public void moveChild(int index) {
        mDelegate.moveChild(index);
    }

    /**
     * Moves to bottom(VERTICAL) or right(HORIZONTAL) of child view
     *
     * @param index        index child view index
     * @param duration
     * @param interpolator
     */
    public void moveChild(int index, long duration, TimeInterpolator interpolator) {
        mDelegate.moveChild(index, duration, interpolator);
    }

    /**
     * @deprecated Use {@link #setExpanseOrientation(int)} instead
     */
    public void setOrientation(@Orientation final int orientation) {
        setExpanseOrientation(orientation);
    }

    /**
     * Sets orientation of expanse animation.
     *
     * @param orientation Set 0 if orientation is horizontal, 1 if orientation is vertical
     */
    public void setExpanseOrientation(@Orientation final int orientation) {
        mDelegate.setExpanseOrientation(orientation);
    }

    /**
     * Gets the width from left of layout if orientation is horizontal.
     * Gets the height from top of layout if orientation is vertical.
     *
     * @param index index of child view
     * @return position from top or left
     */
    public int getChildPosition(final int index) {
        return mDelegate.getChildPosition(index);
    }

    /**
     * Gets the width from left of layout if orientation is horizontal.
     * Gets the height from top of layout if orientation is vertical.
     *
     * @return
     */
    public int getClosePosition() {
        return mDelegate.getClosePosition();
    }

    /**
     * Sets the close position directly.
     *
     * @param position
     * @see #setClosePositionIndex(int)
     */
    public void setClosePosition(final int position) {
        mDelegate.setClosePosition(position);
    }

    /**
     * Gets the current position.
     *
     * @return
     */
    public int getCurrentPosition() {
        return mDelegate.getCurrentPosition();
    }

    /**
     * Sets close position using index of child view.
     *
     * @param childIndex
     * @see #setClosePosition(int)
     */
    public void setClosePositionIndex(final int childIndex) {
        mDelegate.setClosePositionIndex(childIndex);
    }
}