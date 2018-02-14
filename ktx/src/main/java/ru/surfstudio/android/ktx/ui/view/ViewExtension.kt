package ru.surfstudio.android.ktx.ui.view

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.support.annotation.ColorInt
import android.view.View

/**
 * Extension-методы для View
 */


/**
 * Меняет цвет background у view
 */
fun View.changeViewBackgroundColor(@ColorInt color: Int) {
    val background = background.mutate()
    when (background) {
        is ShapeDrawable -> background.paint.color = color
        is GradientDrawable -> background.setColor(color)
        is ColorDrawable -> background.color = color
    }
}


fun View.setVisible(visible: Boolean) {
    if (visible) {
        visibility = View.VISIBLE
        isEnabled = true
    } else {
        visibility = View.INVISIBLE
        isEnabled = false
    }
}

fun View.setVisibleOrGone(visible: Boolean) {
    if (visible) {
        visibility = View.VISIBLE
        isEnabled = true
    } else {
        visibility = View.GONE
        isEnabled = false
    }
}