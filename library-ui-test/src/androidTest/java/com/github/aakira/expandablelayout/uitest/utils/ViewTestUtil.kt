package com.github.aakira.expandablelayout.uitest.utils

import android.view.View
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

fun orMoreHeight(height: Int) = object : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description) {
        description.appendText(String.format("The height of this layout " +
                "is not or more height (%d).", height))
    }

    override fun matchesSafely(view: View) = view.height > height
}

fun equalHeight(height: Int) = object : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description) {
        description.appendText(String.format("The height of this layout " +
                "is not equal to height(%d).", height))
    }

    override fun matchesSafely(view: View) = view.height == height
}

fun equalHeight(vararg views: View) = object : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description) {
        description.appendText(buildString {
            append("The height of this layout is not equal to view height(")
            views.forEachIndexed { i, view ->
                append(view.height)
                if (i != views.size - 1) append(" + ")
            }
            append(" = ")
            append(views.sumBy { it.height })
            append(")")
        })
    }

    override fun matchesSafely(view: View) = view.height == views.sumBy { it.height }
}
