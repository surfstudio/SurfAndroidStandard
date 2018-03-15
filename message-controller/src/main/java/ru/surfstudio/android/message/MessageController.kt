package ru.surfstudio.android.message

import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.view.Gravity
import android.view.View

/**
 * Интерфейс контроллера отображения сообщений
 * Максимальное количество линий задается в integers:design_snackbar_text_max_lines
 */
private val DEFAULT_TOAST_GRAVITY = Gravity.BOTTOM

interface MessageController {

    fun show(message: String,
             @ColorRes backgroundColor: Int? = null,
             @StringRes actionStringId: Int? = null,
             @ColorRes buttonColor: Int? = null,
             listener: (view: View) -> Unit = {})

    fun show(@StringRes stringId: Int,
             @ColorRes backgroundColor: Int? = null,
             @StringRes actionStringId: Int? = null,
             @ColorRes buttonColor: Int? = null,
             listener: (view: View) -> Unit = {})


    fun showToast(@StringRes stringId: Int,
                  gravity: Int = DEFAULT_TOAST_GRAVITY)

    fun showToast(message: String,
                  gravity: Int = DEFAULT_TOAST_GRAVITY)
}