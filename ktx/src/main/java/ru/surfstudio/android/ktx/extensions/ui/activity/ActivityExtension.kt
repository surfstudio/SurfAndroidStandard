package ru.surfstudio.android.ktx.extensions.ui.activity

import android.app.Activity

import android.content.Context
import android.graphics.Rect
import android.view.inputmethod.InputMethodManager
import ru.surfstudio.android.ktx.extensions.ui.context.getDisplayMetrics

/**
 * Extension-методы для Activity
 */

/**
 * Листенер на скрытие / появление клавиатуры
 */
fun Activity.keyboardVisibilityToggleListener(listener: (Boolean) -> Unit) {
    val rootView = window.decorView

    val rect = Rect()
    rootView.getWindowVisibleDisplayFrame(rect)

    val screenHeight = rootView.height
    val keypadHeight = screenHeight - rect.bottom

    listener(keypadHeight > screenHeight * 0.15)
}

/**
 * Скрытие экранной клавиатуры
 */
fun Activity.hideKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

/**
 * Возвращает ширину или высоту экрана
 */
fun Activity.getDisplayParam(param: DisplayParam): Int {
    val metrics = getDisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metrics)

    return when (param) {
        DisplayParam.WIDTH -> metrics.widthPixels
        DisplayParam.HEIGHT -> metrics.heightPixels
    }
}