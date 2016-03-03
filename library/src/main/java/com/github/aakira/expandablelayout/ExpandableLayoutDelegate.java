package com.github.aakira.expandablelayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Delegate which all layouts can share to have the same Expandable behaviour
 */
public class ExpandableLayoutDelegate {

    private ViewGroup viewGroup;

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

    public ExpandableLayoutDelegate(ViewGroup viewGroup) {
        this.viewGroup = viewGroup;
    }

    public void init(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.expandableLayout, defStyleAttr, 0);
        duration = a.getInteger(R.styleable.expandableLayout_ael_duration, ExpandableLayout.DEFAULT_DURATION);
        isExpanded = a.getBoolean(R.styleable.expandableLayout_ael_expanded, ExpandableLayout.DEFAULT_EXPANDED);
        orientation = a.getInteger(R.styleable.expandableLayout_ael_orientation, ExpandableLayout.VERTICAL);
        defaultChildIndex = a.getInteger(R.styleable.expandableLayout_ael_defaultChildIndex,
                Integer.MAX_VALUE);
        defaultChildPosition = a.getInteger(R.styleable.expandableLayout_ael_defaultPosition,
                Integer.MIN_VALUE);
        final int interpolatorType = a.getInteger(R.styleable.expandableLayout_ael_interpolator,
                Utils.LINEAR_INTERPOLATOR);
        interpolator = Utils.createInterpolator(interpolatorType);
        a.recycle();
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!isCalculatedSize) {
            // calculate this layout size
            childPositionList.clear();
            int childSize;
            int childMargin;
            int sumSize = 0;
            View view;
            RelativeLayout.LayoutParams params;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                view = viewGroup.getChildAt(i);
                params = (RelativeLayout.LayoutParams) view.getLayoutParams();

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

    public Parcelable onSaveInstanceState(Parcelable resultOfSuper) {
        final ExpandableSavedState ss = new ExpandableSavedState(resultOfSuper);
        ss.setSize(getCurrentPosition());
        return ss;
    }

    public void onRestoreInstanceState(final ExpandableSavedState resultOfOnRestoreInstanceState) {
        savedState = resultOfOnRestoreInstanceState;
    }

    public void setListener(@NonNull ExpandableLayoutListener listener) {
        this.listener = listener;
    }

    public void toggle() {
        if (closePosition < getCurrentPosition()) {
            collapse();
        } else {
            expand();
        }
    }

    public void expand() {
        if (isAnimating) {
            return;
        }
        createExpandAnimator(getCurrentPosition(), layoutSize,
                duration, interpolator).start();
    }

    public void collapse() {
        if (isAnimating) {
            return;
        }
        createExpandAnimator(getCurrentPosition(), closePosition,
                duration, interpolator).start();
    }

    public void initLayout(final boolean isMaintain) {
        closePosition = 0;
        layoutSize = 0;
        isArranged = isMaintain;
        isCalculatedSize = false;
        savedState = null;

        viewGroup.requestLayout();
    }

    public void setDuration(final int duration) {
        if (duration < 0) {
            throw new IllegalArgumentException("Animators cannot have negative duration: " +
                    duration);
        }
        this.duration = duration;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
        isArranged = false;
        viewGroup.requestLayout();
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setInterpolator(@NonNull final TimeInterpolator interpolator) {
        this.interpolator = interpolator;
    }

    public void move(int position) {
        move(position, duration, interpolator);
    }

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

    public void moveChild(int index) {
        moveChild(index, duration, interpolator);
    }

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
    public void setOrientation(@ExpandableLayout.Orientation final int orientation) {
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
        return isVertical() ? viewGroup.getMeasuredHeight() : viewGroup.getMeasuredWidth();
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
        return orientation == ExpandableLayout.VERTICAL;
    }

    private void setLayoutSize(int size) {
        if (isVertical()) {
            viewGroup.getLayoutParams().height = size;
        } else {
            viewGroup.getLayoutParams().width = size;
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
                    viewGroup.getLayoutParams().height = (int) animator.getAnimatedValue();
                } else {
                    viewGroup.getLayoutParams().width = (int) animator.getAnimatedValue();
                }
                viewGroup.requestLayout();
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
                        ? viewGroup.getLayoutParams().height : viewGroup.getLayoutParams().width;
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
