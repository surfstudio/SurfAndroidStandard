package ru.surfstudio.android.kotlinextensions.ui.view

import android.view.View
import android.view.View.*


/**
 * Файл с extension-методами для работы с UI
 */
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


fun View.setVisible(visible: Boolean) {
    if (visible) {
        visibility = VISIBLE
        isEnabled = true
    } else {
        visibility = INVISIBLE
        isEnabled = false
    }
}

fun View.setVisibleOrGone(visible: Boolean) {
    if (visible) {
        visibility = VISIBLE
        isEnabled = true
    } else {
        visibility = GONE
        isEnabled = false
    }
}
/*
*/
/**
 * Делает тень под тулбаром
 *//*
fun AppBarLayout.showElevation(isElevation: Boolean) {
    if (SdkUtils.isAtLeastLollipop()) {
        elevation = if (isElevation) 8F else 0F
    }
}*/


/*
fun formatPhone(source: String): String? {
    return if (SdkUtils.isAtLeastLollipop()) {
        PhoneNumberUtils.formatNumber(source, Locale.getDefault().country)
    } else {
        PhoneNumberUtils.formatNumber(source)
    }
}*/
