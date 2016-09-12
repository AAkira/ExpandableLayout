package com.github.aakira.expandablelayout.uitest

import android.app.Activity
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.runner.AndroidJUnit4
import android.test.ActivityInstrumentationTestCase2
import com.github.aakira.expandablelayout.ExpandableWeightLayout
import com.github.aakira.expandablelayout.uitest.utils.ElapsedIdLingResource
import com.github.aakira.expandablelayout.uitest.utils.equalHeight
import com.github.aakira.expandablelayout.uitest.utils.equalWeight
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNull.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.CoreMatchers.`is` as _is

@RunWith(AndroidJUnit4::class)
class ExpandableWeightLayoutActivityTest : ActivityInstrumentationTestCase2<ExpandableWeightLayoutActivity>
(ExpandableWeightLayoutActivity::class.java) {

    companion object {
        val DURATION = 1000L
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
    fun testExpandableWeightLayout() {
        val activity = activity
        val instrumentation = instrumentation

        // check activity
        assertThat<Activity>(activity, notNullValue())
        assertThat(instrumentation, notNullValue())

        val expandableLayout = activity.findViewById(R.id.expandableLayout) as ExpandableWeightLayout

        // default close
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(0)))
        assertThat(expandableLayout.isExpanded, _is(false))

        // open toggle
        instrumentation.runOnMainSync { expandableLayout.toggle() }
        var idlingResource = ElapsedIdLingResource(DURATION)
        Espresso.registerIdlingResources(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalWeight(3f)))
        Espresso.unregisterIdlingResources(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(true))

        // change weight
        instrumentation.runOnMainSync { expandableLayout.move(6f) }
        idlingResource = ElapsedIdLingResource(DURATION)
        Espresso.registerIdlingResources(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalWeight(6f)))
        Espresso.unregisterIdlingResources(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(true))

        // quick change a weight using move method
        instrumentation.runOnMainSync { expandableLayout.move(2f, 0, null) }
        idlingResource = ElapsedIdLingResource(0)
        Espresso.registerIdlingResources(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalWeight(2f)))
        Espresso.unregisterIdlingResources(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(true))

        // quick collapse
        instrumentation.runOnMainSync { expandableLayout.collapse(0, null) }
        idlingResource = ElapsedIdLingResource(0)
        Espresso.registerIdlingResources(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalWeight(0f)))
        Espresso.unregisterIdlingResources(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(false))

        // set expanse (default expanse weight is 3)
        instrumentation.runOnMainSync { expandableLayout.isExpanded = true }
        idlingResource = ElapsedIdLingResource(DURATION)
        Espresso.registerIdlingResources(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalWeight(3f)))
        Espresso.unregisterIdlingResources(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(true))

        // check init layout
        instrumentation.runOnMainSync {
            expandableLayout.setExpandWeight(10f)
            expandableLayout.expand()
        }
        idlingResource = ElapsedIdLingResource(DURATION)
        Espresso.registerIdlingResources(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalWeight(10f)))
        Espresso.unregisterIdlingResources(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(true))
    }
}