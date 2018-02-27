package ru.surfstudio.android.utilktx.ktx.ui.view

import android.graphics.drawable.Drawable
import android.widget.ImageView

/**
 * Extension-функции для работы с ImageView.
 */

/**
 * Установка изображения из ресурсов или скрытие [ImageView] если ([drawable] == null).
 */
fun ImageView.setImageDrawableOrGone(drawable: Drawable?) {
    goneIf(drawable == null)
    setImageDrawable(drawable)
}