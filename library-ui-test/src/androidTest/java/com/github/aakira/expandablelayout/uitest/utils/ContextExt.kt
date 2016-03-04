package com.github.aakira.expandablelayout.uitest.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

fun Context.getDisplayMetrics(): DisplayMetrics {
    val metrics = DisplayMetrics()
    (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(metrics)
    return metrics
}