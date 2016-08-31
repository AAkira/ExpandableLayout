package com.github.aakira.expandablelayout.uitest.utils

import android.view.View
import android.widget.LinearLayout
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

fun equalHeight(vararg views: View, margin: Int? = null) = object : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description) {
        description.appendText(buildString {
            append("The height of this layout is not equal to view height(")
            views.forEachIndexed { i, view ->
                append(view.height)
                if (i != views.size - 1) append(" + ")
            }
            margin?.let { append(" + margin:" + it) }
            if (views.size > 0) {
                append(" = ")
                var sumHeight = views.sumBy { it.height }
                margin?.let { sumHeight += it }
                append(sumHeight)
            }
            append(")")
        })
    }

    override fun matchesSafely(view: View) = view.height == views.sumBy { it.height } + (margin ?: 0)
}

fun equalWidth(width: Int) = object : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description) {
        description.appendText(String.format("The width of this layout " +
                "is not equal to width(%d).", width))
    }

    override fun matchesSafely(view: View) = view.width == width
}

fun equalWeight(weight: Float) = object : TypeSafeMatcher<View>() {
    var viewWeight = 0f

    override fun describeTo(description: Description) {
        description.appendText(String.format("The weight(%f) of this layout " +
                "is not equal to weight(%f).", viewWeight, weight))
    }

    override fun matchesSafely(view: View): Boolean {
        (view.layoutParams as LinearLayout.LayoutParams).apply {
            viewWeight = weight
        }.run {
            return (view.layoutParams as LinearLayout.LayoutParams).weight == weight
        }
    }
}