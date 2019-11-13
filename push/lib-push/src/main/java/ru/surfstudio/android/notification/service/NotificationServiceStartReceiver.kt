package ru.surfstudio.android.notification.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ru.surfstudio.android.logger.Logger

/**
 * Receives intent from [NotificationReviverService]
 * and on first boot; need to restart service
 * for keeping app alive in background (for example, receiving pushes)
 */
class NotificationServiceStartReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Logger.d("NotificationServiceStartReceiver :: onReceive")
        context?.let {
            NotificationReviverService.startServiceWithCheck(it)
        }
    }
}