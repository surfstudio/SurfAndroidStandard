package ru.surfstudio.android.ktx.ui.view

import android.support.design.widget.AppBarLayout
import ru.surfstudio.android.core.util.SdkUtils


/**
 * Файл с extension-методами для работы с UI
 */




/**
 * Делает тень под тулбаром
 */
fun AppBarLayout.showElevation(isElevation: Boolean) {
    if (SdkUtils.isAtLeastLollipop()) {
        elevation = if (isElevation) 8F else 0F
    }
}



