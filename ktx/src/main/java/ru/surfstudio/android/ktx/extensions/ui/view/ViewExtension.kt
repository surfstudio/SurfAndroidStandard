package ru.surfstudio.android.ktx.extensions.ui.view

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

fun View.invisibleIf(invisible: Boolean) {
    if (invisible) {
        visibility = View.VISIBLE
        isEnabled = true
    } else {
        visibility = View.INVISIBLE
        isEnabled = false
    }
}

fun View.goneIf(gone: Boolean) {
    if (gone) {
        visibility = View.GONE
        isEnabled = false
    } else {
        visibility = View.VISIBLE
        isEnabled = true
    }
}

/**
 * Extension метод, который запускает action если данные изменились
 */
fun <T : View, R> T.actionIfChanged(data: R, action: T.(data: R) -> Unit) {
    val hash = data?.hashCode()
    if (this.tag != hash) {
        action(data)
        this.tag = hash
    }
}