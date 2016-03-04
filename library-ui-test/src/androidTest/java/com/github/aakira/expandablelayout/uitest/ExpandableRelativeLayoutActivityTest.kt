package com.github.aakira.expandablelayout.uitest

import android.app.Activity
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.runner.AndroidJUnit4
import android.test.ActivityInstrumentationTestCase2
import com.github.aakira.expandablelayout.ExpandableRelativeLayout
import com.github.aakira.expandablelayout.uitest.utils.ElapsedIdLingResource
import com.github.aakira.expandablelayout.uitest.utils.equalHeight
import com.github.aakira.expandablelayout.uitest.utils.orMoreHeight
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNull.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.CoreMatchers.`is` as _is

@RunWith(AndroidJUnit4::class)
class ExpandableRelativeLayoutActivityTest : ActivityInstrumentationTestCase2<ExpandableRelativeLayoutActivity>
(ExpandableRelativeLayoutActivity::class.java) {

    companion object {
        val DURATION = 500L
    }

    @Before
    @Throws(Exception::class)
    public override fun setUp() {
        super.setUp()
        injectInstrumentation(InstrumentationRegistry.getInstrumentation())
    }

    @After
    @Throws(Exception::class)
    public override fun tearDown() {
        super.tearDown()
    }

    @Test
    fun testExpandableLayout() {
        val activity = activity
        val instrumentation = instrumentation

        // check activity
        assertThat<Activity>(activity, notNullValue())
        assertThat(instrumentation, notNullValue())

        val expandableLayout = activity.findViewById(R.id.expandableLayout) as ExpandableRelativeLayout

        // default close
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(0)))

        // open toggle
        instrumentation.runOnMainSync { expandableLayout.toggle() }
        var idlingResource = ElapsedIdLingResource(DURATION)
        Espresso.registerIdlingResources(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(orMoreHeight(1)))
        Espresso.unregisterIdlingResources(idlingResource)

        // move to first layout
        instrumentation.runOnMainSync { expandableLayout.moveChild(0) }
        idlingResource = ElapsedIdLingResource(DURATION)
        Espresso.registerIdlingResources(idlingResource)
        onView(withId(R.id.first)).check(matches(equalHeight(expandableLayout)))
        Espresso.unregisterIdlingResources(idlingResource)

        // set close height
        instrumentation.runOnMainSync { expandableLayout.closePosition = expandableLayout.currentPosition; }

        // move to second layout
        instrumentation.runOnMainSync { expandableLayout.moveChild(1) }
        idlingResource = ElapsedIdLingResource(DURATION)
        Espresso.registerIdlingResources(idlingResource)
        // second.height != 0 && first.height + second.height == expandableLayout.height
        onView(withId(R.id.second)).check(matches(orMoreHeight(1)))
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(
                activity.findViewById(R.id.first),
                activity.findViewById(R.id.second)
        )))
        Espresso.unregisterIdlingResources(idlingResource)

        // check toggle (close to first)
        instrumentation.runOnMainSync { expandableLayout.toggle() }
        idlingResource = ElapsedIdLingResource(DURATION)
        Espresso.registerIdlingResources(idlingResource)
        // move to first position
        onView(withId(R.id.first)).check(matches(equalHeight(expandableLayout)))
        Espresso.unregisterIdlingResources(idlingResource)

        // check toggle open (full)
        instrumentation.runOnMainSync { expandableLayout.toggle() }
        idlingResource = ElapsedIdLingResource(DURATION)
        Espresso.registerIdlingResources(idlingResource)
        // move to first position
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(
                activity.findViewById(R.id.first),
                activity.findViewById(R.id.second),
                activity.findViewById(R.id.third)
        )))
        Espresso.unregisterIdlingResources(idlingResource)
    }
}