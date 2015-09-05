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
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import jp.android.aakira.expandablelayout.R;

public class ExpandableWeightLayout extends RelativeLayout implements ExpandableLayout {

    private int duration;
    private Boolean isDefaultVisibility;
    private TimeInterpolator interpolator = new LinearInterpolator();

    private ExpandableLayoutListener listener;
    private ExpandableSavedState savedState;
    private float layoutWeight = 0.0f;
    private Boolean isArranged = false;
    private Boolean isAnimating = false;
    private Boolean isWeightLayout = false;

    public ExpandableWeightLayout(final Context context) {
        this(context, null);
    }

    public ExpandableWeightLayout(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableWeightLayout(final Context context, final AttributeSet attrs,
                                  final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExpandableWeightLayout(final Context context, final AttributeSet attrs,
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
        final int interpolatorType = a.getInteger(R.styleable.expandableLayout_interpolator,
                Utils.LINEAR_INTERPOLATOR);
        interpolator = Utils.createInterpolator(interpolatorType);
        a.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // Check this layout using the attribute of weight
        if (!(getLayoutParams() instanceof LinearLayout.LayoutParams)) {
            return;
        }
        if (0 < ((LinearLayout.LayoutParams) getLayoutParams()).weight) {
            isWeightLayout = 0 < ((LinearLayout.LayoutParams) getLayoutParams()).weight;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isArranged) {
            return;
        }
        if (!isWeightLayout) {
            return;
        }

        layoutWeight = ((LinearLayout.LayoutParams) getLayoutParams()).weight;
        if (0 < layoutWeight) {
            isArranged = true;
        }

        if (!isDefaultVisibility) {
            ((LinearLayout.LayoutParams) getLayoutParams()).weight = 0;
        }
        if (savedState == null) {
            return;
        }
        ((LinearLayout.LayoutParams) getLayoutParams()).weight = savedState.getWeight();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable parcelable = super.onSaveInstanceState();
        if (!isWeightLayout) {
            return parcelable;
        }
        final ExpandableSavedState ss = new ExpandableSavedState(parcelable);
        ss.setWeight(((LinearLayout.LayoutParams) getLayoutParams()).weight);
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
        if (!isWeightLayout) {
            return;
        }
        if (0 < ((LinearLayout.LayoutParams) getLayoutParams()).weight) {
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
        createExpandAnimator(0, layoutWeight).start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void collapse() {
        if (isAnimating) {
            return;
        }
        createExpandAnimator(layoutWeight, 0).start();
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
    public void setDefaultVisibility(final boolean defaultVisibility) {
        this.isDefaultVisibility = defaultVisibility;
    }

    /**
     * Creates value animator.
     * Expand the layout if @param.to is bigger than @param.from.
     * Collapse the layout if @param.from is bigger than @param.to.
     *
     * @param from
     * @param to
     * @return
     */
    public ValueAnimator createExpandAnimator(final float from, final float to) {
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                if (!isWeightLayout) {
                    return;
                }
                ((LinearLayout.LayoutParams) getLayoutParams()).weight =
                        (float) animation.getAnimatedValue();
                requestLayout();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimating = true;

                if (listener == null) {
                    return;
                }
                listener.onAnimationStart();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;

                if (listener == null) {
                    return;
                }
                listener.onAnimationEnd();

                if (!isWeightLayout) {
                    return;
                }

                final float currentWeight = ((LinearLayout.LayoutParams) getLayoutParams()).weight;
                if (currentWeight == layoutWeight) {
                    listener.onOpened();
                    return;
                }
                if (currentWeight == 0) {
                    listener.onClosed();
                }
            }
        });
        return valueAnimator;
    }
}