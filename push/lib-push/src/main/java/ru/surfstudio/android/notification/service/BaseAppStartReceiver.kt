package ru.surfstudio.android.notification.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ru.surfstudio.android.logger.Logger

/**
 * Базовый ресивер, куда приходит ивент о закрытии сервиса [NotificationReviverService]
 */
abstract class BaseAppStartReceiver: BroadcastReceiver() {

    abstract fun getNotificationProcessStarter(): BaseNotificationProcessStarter

    override fun onReceive(context: Context?, intent: Intent?) {
        Logger.d("AppStartReceiver :: onReceive")
        getNotificationProcessStarter().startNotificationServiceReviver()
    }
}