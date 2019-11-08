package ru.surfstudio.android.notification.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Базовый ресивер куда приходит ивент о закрытии сервиса [NotificationReviverService]
 */
abstract class BaseAppStartReceiver<S : BaseNotificationProcessStarter>: BroadcastReceiver() {

    abstract fun getNotificationProcessStarter(): S

    override fun onReceive(context: Context?, intent: Intent?) {
        getNotificationProcessStarter().startNotificationServiceReviver()
    }
}