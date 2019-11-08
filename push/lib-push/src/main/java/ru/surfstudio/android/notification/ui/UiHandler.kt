package ru.surfstudio.android.notification.ui

import android.os.Handler
import android.os.Looper

/**
 * Утил класс для запуска действия на главном потоке
 */
object UiHandler {

    private val applicationHandler: Handler = Handler(Looper.getMainLooper())

    fun runOnUI(delay: Long = 0L, action: () -> Unit) {
        applicationHandler.postDelayed(action, delay)
    }
}