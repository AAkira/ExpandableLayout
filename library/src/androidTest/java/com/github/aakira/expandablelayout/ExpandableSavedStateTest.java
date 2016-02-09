package com.github.aakira.expandablelayout;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.test.runner.AndroidJUnit4;
import android.view.AbsSavedState;
import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class ExpandableSavedStateTest {

    @Test
    public void testWriteToParcel() {
        final Parcelable parcelable = new View.BaseSavedState(AbsSavedState.EMPTY_STATE);
        final ExpandableSavedState ss = new ExpandableSavedState(parcelable);
        ss.setSize(1000);
        ss.setWeight(0.5f);

        final Parcel parcel = Parcel.obtain();
        ss.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        final ExpandableSavedState target = ExpandableSavedState.CREATOR.createFromParcel(parcel);
        assertThat(target.getSize(), is(1000));
        assertThat(target.getWeight(), is(0.5f));
    }
}