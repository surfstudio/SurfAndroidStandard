package ru.surfstudio.android.recycler.extension.util

import android.content.Context
import android.util.TypedValue

/**
 * Extension-method for fast dp to px convertation.
 * */
internal fun Context.dpToPx(dp: Float): Int {
    return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            resources.displayMetrics
    ).toInt()
}

/**
 * Extension-method for fast dp to px convertation.
 * */
internal fun Context.dpToPx(dp: Int): Int {
    return dpToPx(dp.toFloat())
}