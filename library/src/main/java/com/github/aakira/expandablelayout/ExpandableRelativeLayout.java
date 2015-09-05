package com.github.aakira.expandablelayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import jp.android.aakira.expandablelayout.R;

public class ExpandableRelativeLayout extends RelativeLayout implements ExpandableLayout {

    private int duration;
    private Boolean isDefaultVisibility;
    private TimeInterpolator interpolator = new LinearInterpolator();
    private int orientation;
    /**
     * The close position is width from left of layout if orientation is horizontal.
     * The close position is height from top of layout if orientation is vertical.
     */
    private int closePosition = 0;

    private ExpandableLayoutListener listener;
    private ExpandableSavedState savedState;
    private int layoutSize = 0;
    private Boolean isArranged = false;
    private Boolean isAnimating = false;
    private List<Integer> childPositionList = new ArrayList<>();

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
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.expandableLayout, defStyleAttr, 0);
        duration = a.getInteger(R.styleable.expandableLayout_duration, DEFAULT_DURATION);
        isDefaultVisibility = a.getBoolean(R.styleable.expandableLayout_defaultVisibility,
                DEFAULT_VISIBILITY);
        orientation = a.getInteger(R.styleable.expandableLayout_orientation, VERTICAL);
        final int interpolatorType = a.getInteger(R.styleable.expandableLayout_interpolator,
                Utils.LINEAR_INTERPOLATOR);
        interpolator = Utils.createInterpolator(interpolatorType);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isArranged) {
            return;
        }

        childPositionList.clear();
        int childSize;
        int childMargin;
        int sumSize = 0;
        for (int i = 0; i < getChildCount(); i++) {
            final View view = getChildAt(i);
            final LayoutParams params = (LayoutParams) view.getLayoutParams();

            childSize = isVertical()
                    ? view.getMeasuredHeight() : view.getMeasuredWidth();
            childMargin = isVertical()
                    ? params.topMargin + params.bottomMargin
                    : params.leftMargin + params.rightMargin;
            if (0 < i) {
                sumSize = childPositionList.get(i - 1);
            }
            childPositionList.add(sumSize + childSize + childMargin);
        }
        layoutSize = isVertical() ? getMeasuredHeight() : getMeasuredWidth();

        if (0 < layoutSize) {
            isArranged = true;
        }

        if (!isDefaultVisibility) {
            getLayoutParams().height = 0;
        }
        if (savedState == null) {
            return;
        }
        if (isVertical()) {
            getLayoutParams().height = savedState.getSize();
        } else {
            getLayoutParams().width = savedState.getSize();
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable parcelable = super.onSaveInstanceState();
        final ExpandableSavedState ss = new ExpandableSavedState(parcelable);
        if (isVertical()) {
            ss.setSize(getHeight());
        } else {
            ss.setSize(getWidth());
        }
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(final Parcelable state) {
        if (!(state instanceof ExpandableSavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        final ExpandableSavedState ss = (ExpandableSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        savedState = ss;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setListener(ExpandableLayoutListener listener) {
        this.listener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toggle() {
        if (closePosition < getMeasuredHeight()) {
            collapse();
        } else {
            expand();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void expand() {
        if (isAnimating) {
            return;
        }
        createExpandAnimator(getMeasuredHeight(), layoutSize).start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void collapse() {
        if (isAnimating) {
            return;
        }
        createExpandAnimator(getMeasuredHeight(), closePosition).start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDuration(@NonNull final int duration) {
        if (duration < 0) {
            throw new IllegalArgumentException("Animators cannot have negative duration: " +
                    duration);
        }
        this.duration = duration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInterpolator(@NonNull final TimeInterpolator interpolator) {
        this.interpolator = interpolator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaultVisibility(@NonNull final boolean defaultVisibility) {
        this.isDefaultVisibility = defaultVisibility;
    }

    /**
     * Moves to position
     *
     * @param position
     */
    public void move(int position) {
        if (isAnimating) {
            return;
        }
        if (0 > position || layoutSize < position) {
            return;
        }
        createExpandAnimator(getMeasuredHeight(), position).start();
    }

    /**
     * Moves to bottom(VERTICAL) or right(HORIZONTAL) of child view
     *
     * @param index child view index
     */
    public void moveChild(int index) {
        if (isAnimating) {
            return;
        }
        createExpandAnimator(getMeasuredHeight(), childPositionList.get(index)).start();
    }

    /**
     * Sets orientation of expanse animation.
     *
     * @param orientation Set 0 if orientation is horizontal, 1 if orientation is vertical
     */
    public void setOrientation(@Orientation final int orientation) {
        this.orientation = orientation;
    }

    /**
     * Gets the width from left of layout if orientation is horizontal.
     * Gets the height from top of layout if orientation is vertical.
     *
     * @param index index of child view
     * @return position from top or left
     */
    public int getChildPosition(final int index) {
        if (0 > index || childPositionList.size() <= index) {
            throw new IllegalArgumentException("There aren't the view having this index.");
        }
        return childPositionList.get(index);
    }

    /**
     * Gets the width from left of layout if orientation is horizontal.
     * Gets the height from top of layout if orientation is vertical.
     *
     * @return
     * @see #closePosition
     */
    public int getClosePosition() {
        return closePosition;
    }

    /**
     * Sets the close position directly.
     *
     * @param position
     * @see #closePosition
     * @see #setClosePositionIndex(int)
     */
    public void setClosePosition(final int position) {
        this.closePosition = position;
    }

    /**
     * Gets the current position.
     *
     * @return
     */
    public int getCurrentPosition() {
        return getMeasuredHeight();
    }

    /**
     * Sets close position using index of child view.
     *
     * @param childIndex
     * @see #closePosition
     * @see #setClosePosition(int)
     */
    public void setClosePositionIndex(final int childIndex) {
        this.closePosition = getChildPosition(childIndex);
    }

    private boolean isVertical() {
        return orientation == VERTICAL;
    }

    /**
     * Creates value animator.
     * Expand the layout if {@param to} is bigger than {@param from}.
     * Collapse the layout if {@param from} is bigger than {@param to}.
     *
     * @param from
     * @param to
     * @return
     */
    private ValueAnimator createExpandAnimator(final int from, final int to) {
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(from, to);
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator animator) {
                if (isVertical()) {
                    getLayoutParams().height = (int) animator.getAnimatedValue();
                } else {
                    getLayoutParams().width = (int) animator.getAnimatedValue();
                }
                requestLayout();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animator) {
                isAnimating = true;

                if (listener == null) {
                    return;
                }
                listener.onAnimationStart();
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isAnimating = false;
                if (listener == null) {
                    return;
                }
                listener.onAnimationEnd();


                final int currentSize = isVertical()
                        ? getLayoutParams().height : getLayoutParams().width;
                if (currentSize == layoutSize) {
                    listener.onOpened();
                    return;
                }
                if (currentSize == closePosition) {
                    listener.onClosed();
                }
            }
        });
        return valueAnimator;
    }
}