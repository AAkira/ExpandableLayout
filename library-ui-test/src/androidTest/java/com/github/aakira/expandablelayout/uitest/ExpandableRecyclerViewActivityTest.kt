package com.github.aakira.expandablelayout.uitest

import android.app.Activity
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.test.ActivityInstrumentationTestCase2
import android.view.View
import com.github.aakira.expandablelayout.uitest.ExpandableRecyclerViewActivity.RecyclerViewAdapter.ExpandableViewHolder
import com.github.aakira.expandablelayout.uitest.utils.ElapsedIdLingResource
import org.hamcrest.Description
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsNull.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.CoreMatchers.`is` as _is

@RunWith(AndroidJUnit4::class)
class ExpandableRecyclerViewActivityTest : ActivityInstrumentationTestCase2<ExpandableRecyclerViewActivity>
(ExpandableRecyclerViewActivity::class.java) {

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
    fun testExpandableRecyclerViewActivity() {
        val activity = activity
        val instrumentation = instrumentation

        // check activity
        assertThat<Activity>(activity, notNullValue())
        assertThat(instrumentation, notNullValue())

        // count items in recycler view for test
        val recyclerView = activity.findViewById(R.id.recyclerView) as RecyclerView
        assertThat(recyclerView.adapter.itemCount, _is(101))

        val duration = ExpandableRecyclerViewActivity.DURATION + 100// add scroll buffer(100)

        // expand : 0
        onView(withId(R.id.recyclerView)).perform(expand(0))
        var idlingResource = ElapsedIdLingResource(duration)
        Espresso.registerIdlingResources(idlingResource)
        onView(withId(R.id.recyclerView)).check(matches(correctHeight(0, true)))
        Espresso.unregisterIdlingResources(idlingResource)

        // scroll
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.scrollToPosition<ExpandableViewHolder>(100))
        idlingResource = ElapsedIdLingResource(duration)
        Espresso.registerIdlingResources(idlingResource)
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.scrollToPosition<ExpandableViewHolder>(0))
        Espresso.unregisterIdlingResources(idlingResource)

        // restore expanded : 0
        idlingResource = ElapsedIdLingResource(duration)
        Espresso.registerIdlingResources(idlingResource)
        onView(withId(R.id.recyclerView)).check(matches(correctHeight(0, true)))
        Espresso.unregisterIdlingResources(idlingResource)

        // expand : 16
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.scrollToPosition<ExpandableViewHolder>(21))
        onView(withId(R.id.recyclerView)).perform(expand(16))
        idlingResource = ElapsedIdLingResource(duration)
        Espresso.registerIdlingResources(idlingResource)
        onView(withId(R.id.recyclerView)).check(matches(correctHeight(16, true)))
        Espresso.unregisterIdlingResources(idlingResource)

        // expand : 17
        onView(withId(R.id.recyclerView)).perform(expand(17))
        idlingResource = ElapsedIdLingResource(duration)
        Espresso.registerIdlingResources(idlingResource)
        onView(withId(R.id.recyclerView)).check(matches(correctHeight(17, true)))
        Espresso.unregisterIdlingResources(idlingResource)

        // expand : 53
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.scrollToPosition<ExpandableViewHolder>(55))
        onView(withId(R.id.recyclerView)).perform(expand(53))
        idlingResource = ElapsedIdLingResource(duration)
        Espresso.registerIdlingResources(idlingResource)
        onView(withId(R.id.recyclerView)).check(matches(correctHeight(53, true)))
        Espresso.unregisterIdlingResources(idlingResource)

        // scroll
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.scrollToPosition<ExpandableViewHolder>(15))
        // restore expanded : 16, 17
        idlingResource = ElapsedIdLingResource(duration)
        Espresso.registerIdlingResources(idlingResource)
        onView(withId(R.id.recyclerView)).check(matches(correctHeight(16, true)))
        onView(withId(R.id.recyclerView)).check(matches(correctHeight(17, true)))
        Espresso.unregisterIdlingResources(idlingResource)

        // collapse : 16
        onView(withId(R.id.recyclerView)).perform(collapse(16))
        idlingResource = ElapsedIdLingResource(duration)
        Espresso.registerIdlingResources(idlingResource)
        onView(withId(R.id.recyclerView)).check(matches(correctHeight(16, false)))
        Espresso.unregisterIdlingResources(idlingResource)

        // scroll
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.scrollToPosition<ExpandableViewHolder>(0))
        // restore expanded : 0
        idlingResource = ElapsedIdLingResource(duration)
        Espresso.registerIdlingResources(idlingResource)
        onView(withId(R.id.recyclerView)).check(matches(correctHeight(0, true)))
        Espresso.unregisterIdlingResources(idlingResource)

        // scroll
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.scrollToPosition<ExpandableViewHolder>(18))
        // restore collapse : 16
        idlingResource = ElapsedIdLingResource(duration)
        Espresso.registerIdlingResources(idlingResource)
        onView(withId(R.id.recyclerView)).check(matches(correctHeight(16, false)))
        Espresso.unregisterIdlingResources(idlingResource)
    }

    /**
     * must use in recycler view.
     * check a parent height is equal to children height.
     *
     * For example : row item
     *
     * <Parent>
     *     <Child/>
     *     <ExpandableLayout>
     *         <Child/>
     *     </ExpandableLayout>
     * </Parent>
     *
     * @param position
     * @param
     */
    private fun correctHeight(position: Int, isExpanded: Boolean) = object : TypeSafeMatcher<View>() {
        var viewHeight = 0
        var titleHeight = 0
        var expandableLayoutHeight = 0
        var childHeight = 0

        override fun describeTo(description: Description) {
            description.appendText(String.format("The height of expandable(%d) layout is not " +
                    "equal to a height of child(%d). itemHeight(%d), title(%d)",
                    expandableLayoutHeight, childHeight, viewHeight, titleHeight))
        }

        override fun matchesSafely(view: View): Boolean {
            if (view !is RecyclerView) return false


            val holder = view.findViewHolderForAdapterPosition(position) as ExpandableViewHolder
            viewHeight = holder.itemView.height
            titleHeight = holder.title.height
            expandableLayoutHeight = holder.expandableLayout.height
            childHeight = holder.description.height

            return expandableLayoutHeight == childHeight
                    && expandableLayoutHeight == viewHeight - titleHeight
                    && if (isExpanded) expandableLayoutHeight > 0 else expandableLayoutHeight == 0
        }
    }

    fun expand(position: Int) = ViewActions.actionWithAssertions(ExpandAction(position))

    private class ExpandAction(val position: Int) : ViewAction {

        override fun getConstraints() = isDisplayed()

        override fun perform(uiController: UiController, view: View) {
            if (view !is RecyclerView) return

            val holder = view.findViewHolderForAdapterPosition(position) as ExpandableViewHolder
            holder.expandableLayout.expand()
        }

        override fun getDescription() = "Expand."
    }

    fun collapse(position: Int) = ViewActions.actionWithAssertions(CollapseAction(position))

    private class CollapseAction(val position: Int) : ViewAction {

        override fun getConstraints() = isDisplayed()

        override fun perform(uiController: UiController, view: View) {
            if (view !is RecyclerView) return

            val holder = view.findViewHolderForAdapterPosition(position) as ExpandableViewHolder
            holder.expandableLayout.collapse()
        }

        override fun getDescription() = "Collapse."
    }
}