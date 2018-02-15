package ru.surfstudio.android.util


import android.os.Build

/**
 * Утилиты для проверки версии Api
 */
object SdkUtils {

    val isPreLollipop: Boolean
        get() = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP

    val isAtLeastLollipop: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

    val isAtLeastMarshmallow: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    val isAtLeastNougat: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

    val isAtLeastOreo: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    fun doIfSdk(atLeast: Boolean, ifTrue: () -> Unit, ifFalse: () -> Unit) =
            if (atLeast) ifTrue() else ifFalse()
}
