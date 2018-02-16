package ru.surfstudio.android.utilktx.ktx.ui.activity

import android.app.Activity

import android.graphics.Rect
import ru.surfstudio.android.utilktx.ktx.ui.context.getDisplayMetrics
import ru.surfstudio.android.utilktx.util.KeyboardUtil

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
    KeyboardUtil.hideSoftKeyboard(this)

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