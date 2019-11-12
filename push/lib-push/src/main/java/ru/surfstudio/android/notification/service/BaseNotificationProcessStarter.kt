package ru.surfstudio.android.notification.service

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.notification.ui.UiHandler
import ru.surfstudio.android.utilktx.util.SdkUtils

/**
 * Базовый класс для запуска [NotificationReviverService]
 */
abstract class BaseNotificationProcessStarter(
        private val applicationContext: Context
) {

    abstract fun getAppStartReceiverClassName(): String

    fun startNotificationServiceReviver() {
        UiHandler.runOnUI { startServiceInternal() }
    }

    /**
     * Стартует сервис [NotificationReviverService]
     */
    private fun startServiceInternal() {
        if (!SdkUtils.isAtLeastOreo()) {
            applicationContext.startService(Intent(applicationContext, NotificationReviverService::class.java))
        }
    }
}