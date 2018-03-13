package ru.surfstudio.android.utilktx.ktx.ui.view

import android.support.design.widget.AppBarLayout
import ru.surfstudio.android.utilktx.util.SdkUtils


/**
 * Файл с extension-методами для работы с UI
 */

/**
 * Делает тень под тулбаром
 */
fun AppBarLayout.showElevation(isElevation: Boolean) {
    if (SdkUtils.isAtLeastLollipop) {
        elevation = if (isElevation) 8F else 0F
    }
}



