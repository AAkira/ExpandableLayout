package com.github.aakira.expandablelayout.uitest

import android.app.Activity
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.runner.AndroidJUnit4
import android.test.ActivityInstrumentationTestCase2
import android.widget.LinearLayout
import android.widget.TextView
import com.github.aakira.expandablelayout.ExpandableRelativeLayout
import com.github.aakira.expandablelayout.uitest.utils.ElapsedIdLingResource
import com.github.aakira.expandablelayout.uitest.utils.equalHeight
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNull.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.CoreMatchers.`is` as _is

@RunWith(AndroidJUnit4::class)
class ExpandableRelativeLayoutActivityTest3 : ActivityInstrumentationTestCase2<ExpandableRelativeLayoutActivity3>
(ExpandableRelativeLayoutActivity3::class.java) {

    companion object {
        val DURATION = 350L
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
    fun testExpandableRelativeLayoutActivity3() {
        val activity = activity
        val instrumentation = instrumentation

        // check activity
        assertThat<Activity>(activity, notNullValue())
        assertThat(instrumentation, notNullValue())

        val expandableLayout = activity.findViewById(R.id.expandableLayout) as ExpandableRelativeLayout
        val child0 = activity.findViewById(R.id.child0) as LinearLayout
        val child1 = activity.findViewById(R.id.child1) as TextView
        val child2 = activity.findViewById(R.id.child2) as TextView
        val child3 = activity.findViewById(R.id.child3) as TextView

        // default close
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(0)))
        assertThat(expandableLayout.isExpanded, _is(false))

        // open toggle
        instrumentation.runOnMainSync { expandableLayout.toggle() }
        var idlingResource = ElapsedIdLingResource(DURATION)
        Espresso.registerIdlingResources(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(
                child0,
                child2,
                child3
        )))
        Espresso.unregisterIdlingResources(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(true))

        // move to first layout
        instrumentation.runOnMainSync { expandableLayout.moveChild(0) }
        idlingResource = ElapsedIdLingResource(DURATION)
        Espresso.registerIdlingResources(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(
                child0
        )))
        Espresso.unregisterIdlingResources(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(true))

        // move to second layout
        instrumentation.runOnMainSync { expandableLayout.moveChild(1) }
        idlingResource = ElapsedIdLingResource(DURATION)
        Espresso.registerIdlingResources(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(
                child0,
                child1
        )))
        Espresso.unregisterIdlingResources(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(true))

        // move to third layout
        instrumentation.runOnMainSync { expandableLayout.moveChild(2) }
        idlingResource = ElapsedIdLingResource(DURATION)
        Espresso.registerIdlingResources(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(
                child0,
                child2
        )))
        Espresso.unregisterIdlingResources(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(true))

        // move to forth layout
        instrumentation.runOnMainSync { expandableLayout.moveChild(3) }
        idlingResource = ElapsedIdLingResource(DURATION)
        Espresso.registerIdlingResources(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(
                child0,
                child2,
                child3
        )))
        Espresso.unregisterIdlingResources(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(true))

        // quick move to first layout using moveChild method
        instrumentation.runOnMainSync { expandableLayout.moveChild(0, 0, null) }
        idlingResource = ElapsedIdLingResource(0)
        Espresso.registerIdlingResources(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(
                child0
        )))
        Espresso.unregisterIdlingResources(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(true))

        // quick move to 200 using move method
        instrumentation.runOnMainSync { expandableLayout.move(200, 0, null) }
        idlingResource = ElapsedIdLingResource(0)
        Espresso.registerIdlingResources(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(200)))
        Espresso.unregisterIdlingResources(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(true))

        // quick collapse
        instrumentation.runOnMainSync { expandableLayout.collapse(0, null) }
        idlingResource = ElapsedIdLingResource(0)
        Espresso.registerIdlingResources(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(0)))
        Espresso.unregisterIdlingResources(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(false))

        // quick expand
        instrumentation.runOnMainSync { expandableLayout.expand(0, null) }
        idlingResource = ElapsedIdLingResource(0)
        Espresso.registerIdlingResources(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(
                child0,
                child2,
                child3
        )))
        Espresso.unregisterIdlingResources(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(true))
    }
}