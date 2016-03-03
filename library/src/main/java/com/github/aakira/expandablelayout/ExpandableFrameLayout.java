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
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class ExpandableFrameLayout extends FrameLayout implements ExpandableLayout {

    private int duration;
    private boolean isExpanded;
    private TimeInterpolator interpolator = new LinearInterpolator();
    private int orientation;
    /**
     * You cannot define {@link #isExpanded}, {@link #defaultChildIndex}
     * and {@link #defaultChildPosition} at the same time.
     * {@link #defaultChildPosition} has priority over {@link #isExpanded}
     * and {@link #defaultChildIndex} if you set them at the same time.
     */
    private int defaultChildIndex;
    private int defaultChildPosition;
    /**
     * The close position is width from left of layout if orientation is horizontal.
     * The close position is height from top of layout if orientation is vertical.
     */
    private int closePosition = 0;

    private ExpandableLayoutListener listener;
    private ExpandableSavedState savedState;
    private int layoutSize = 0;
    private boolean isArranged = false;
    private boolean isCalculatedSize = false;
    private boolean isAnimating = false;
    private List<Integer> childPositionList = new ArrayList<>();

    public ExpandableFrameLayout(final Context context) {
        this(context, null);
    }

    public ExpandableFrameLayout(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableFrameLayout(final Context context, final AttributeSet attrs,
                                    final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExpandableFrameLayout(final Context context, final AttributeSet attrs,
                                    final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.expandableLayout, defStyleAttr, 0);
        duration = a.getInteger(R.styleable.expandableLayout_ael_duration, DEFAULT_DURATION);
        isExpanded = a.getBoolean(R.styleable.expandableLayout_ael_expanded, DEFAULT_EXPANDED);
        orientation = a.getInteger(R.styleable.expandableLayout_ael_orientation, VERTICAL);
        defaultChildIndex = a.getInteger(R.styleable.expandableLayout_ael_defaultChildIndex,
                Integer.MAX_VALUE);
        defaultChildPosition = a.getInteger(R.styleable.expandableLayout_ael_defaultPosition,
                Integer.MIN_VALUE);
        final int interpolatorType = a.getInteger(R.styleable.expandableLayout_ael_interpolator,
                Utils.LINEAR_INTERPOLATOR);
        interpolator = Utils.createInterpolator(interpolatorType);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!isCalculatedSize) {
            // calculate this layout size
            childPositionList.clear();
            int childSize;
            int childMargin;
            int sumSize = 0;
            View view;
            LayoutParams params;
            for (int i = 0; i < getChildCount(); i++) {
                view = getChildAt(i);
                params = (LayoutParams) view.getLayoutParams();

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
            layoutSize = childPositionList.get(childPositionList.size() - 1);

            if (0 < layoutSize) {
                isCalculatedSize = true;
            }
        }

        if (isArranged) {
            return;
        }

        if (isExpanded) {
            setLayoutSize(layoutSize);
        } else {
            setLayoutSize(closePosition);
        }
        final int childNumbers = childPositionList.size();
        if (childNumbers > defaultChildIndex && childNumbers > 0) {
            moveChild(defaultChildIndex, 0, null);
        }
        if (defaultChildPosition > 0 && layoutSize >= defaultChildPosition && layoutSize > 0) {
            move(defaultChildPosition, 0, null);
        }
        isArranged = true;

        if (savedState == null) {
            return;
        }
        setLayoutSize(savedState.getSize());
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable parcelable = super.onSaveInstanceState();
        final ExpandableSavedState ss = new ExpandableSavedState(parcelable);
        ss.setSize(getCurrentPosition());
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
    public void setListener(@NonNull ExpandableLayoutListener listener) {
        this.listener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toggle() {
        if (closePosition < getCurrentPosition()) {
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
        createExpandAnimator(getCurrentPosition(), layoutSize,
                duration, interpolator).start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void collapse() {
        if (isAnimating) {
            return;
        }
        createExpandAnimator(getCurrentPosition(), closePosition,
                duration, interpolator).start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initLayout(final boolean isMaintain) {
        closePosition = 0;
        layoutSize = 0;
        isArranged = isMaintain;
        isCalculatedSize = false;
        savedState = null;

        super.requestLayout();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDuration(final int duration) {
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
    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
        isArranged = false;
        requestLayout();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExpanded() {
        return isExpanded;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInterpolator(@NonNull final TimeInterpolator interpolator) {
        this.interpolator = interpolator;
    }

    /**
     * @param position
     * @see #move(int, long, TimeInterpolator)
     */
    public void move(int position) {
        move(position, duration, interpolator);
    }

    /**
     * Moves to position
     *
     * @param position
     * @param duration
     * @param interpolator
     */
    public void move(int position, long duration, TimeInterpolator interpolator) {
        if (isAnimating) {
            return;
        }
        if (0 > position || layoutSize < position) {
            return;
        }
        createExpandAnimator(getCurrentPosition(), position,
                duration, interpolator).start();
    }

    /**
     * @param index child view index
     * @see #moveChild(int, long, TimeInterpolator)
     */
    public void moveChild(int index) {
        moveChild(index, duration, interpolator);
    }

    /**
     * Moves to bottom(VERTICAL) or right(HORIZONTAL) of child view
     *
     * @param index        index child view index
     * @param duration
     * @param interpolator
     */
    public void moveChild(int index, long duration, TimeInterpolator interpolator) {
        if (isAnimating) {
            return;
        }
        createExpandAnimator(getCurrentPosition(), childPositionList.get(index),
                duration, interpolator).start();
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
        return isVertical() ? getMeasuredHeight() : getMeasuredWidth();
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

    private void setLayoutSize(int size) {
        if (isVertical()) {
            getLayoutParams().height = size;
        } else {
            getLayoutParams().width = size;
        }
    }

    /**
     * Creates value animator.
     * Expand the layout if {@param to} is bigger than {@param from}.
     * Collapse the layout if {@param from} is bigger than {@param to}.
     *
     * @param from
     * @param to
     * @param duration
     * @param interpolator
     * @return
     */
    private ValueAnimator createExpandAnimator(
            final int from, final int to, final long duration, final TimeInterpolator interpolator) {
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

                if (layoutSize == to) {
                    listener.onPreOpen();
                    return;
                }
                if (closePosition == to) {
                    listener.onPreClose();
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isAnimating = false;
                final int currentSize = isVertical()
                        ? getLayoutParams().height : getLayoutParams().width;
                isExpanded = currentSize > closePosition;

                if (listener == null) {
                    return;
                }
                listener.onAnimationEnd();

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