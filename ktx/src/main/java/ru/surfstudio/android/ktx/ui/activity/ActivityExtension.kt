package ru.surfstudio.android.ktx.ui.activity

import android.app.Activity

import android.content.Context
import android.graphics.Rect
import android.view.inputmethod.InputMethodManager
import ru.surfstudio.android.ktx.ui.context.getDisplayMetrics
/**
 * Extension-методы для Activity
 */

/**
 * Листенер на скрытие / появление клавиатуры
 */
fun Activity.keyboardVisibilityToggleListener(listener: (Boolean)->Unit) {
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


/*
*//**
 * Декорирование статус-бара с учётом всех известных проблем
 *//*
fun Activity.decorateStatusBar() {
    when (ThemeUtil.getThemeType(this)) {
        ThemeUtil.ThemeType.LIGHT -> {
            if (OSUtils.isMiUi()) {
                //в MiUi иконки невозможно покрасить в тёмный цвет. Поэтому на светлой
                //теме красим тулбар в более тёмный, чтобы системные иконки не терялись
                StatusBarUtil.setStatusBarColor(this, R.color.status_bar_21_color)
            }
        }
        ThemeUtil.ThemeType.TRANSLUCENT -> {
            //полупрозрачный статус-бар через стили невозможно перекрасить. для этого
            //используется библиотека SystemBarTint (https://github.com/jgilfelt/SystemBarTint)
            if (SdkUtils.isKitKat()) {
                val tintManager = SystemBarTintManager(this)
                tintManager.isStatusBarTintEnabled = true
                tintManager.setNavigationBarTintEnabled(true)
                //tintManager.setTintColor(R.color.status_bar_translucent_tint_color)
            }
        }
        ThemeUtil.ThemeType.TRANSPARENT -> {
            //в MiUi иконки невозможно покрасить в тёмный цвет. Так как полностью прозрачный
            //статус-бар может использоваться на светлых фонах - красим тулбра в более
            //тёмный, чтобы системные иконки не терялись
            if (OSUtils.isMiUi()) {
                StatusBarUtil.setStatusBarColor(this, R.color.charocoal_transparent)
            }
        }
        ThemeUtil.ThemeType.UNKNOWN -> TODO()
    }
}*/