package ru.surfstudio.android.notification.service

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import ru.surfstudio.android.logger.Logger

/**
 * Empty service which sends broadcast to [NotificationServiceStartReceiver] for restart
 */
class NotificationReviverService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Logger.d("NotificationReviverService :: onStartCommand, flags=$flags, startId=$startId")
        return START_STICKY
    }

    override fun onDestroy() {
        Logger.d("NotificationReviverService :: onDestroy")
        Logger.d("Sending broadcast to NotificationServiceStartReceiver")
        sendBroadcast(Intent(this, NotificationServiceStartReceiver::class.java))
    }

    companion object {

        @Suppress("DEPRECATION")
        private fun isServiceRunning(context: Context): Boolean {
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
                    ?: throw RuntimeException(ActivityManager::class.java.simpleName + " is null")
            return manager.getRunningServices(Integer.MAX_VALUE).find {
                service -> NotificationReviverService::class.java.name == service.service.className
            } != null
        }

        fun startServiceWithCheck(context: Context) {
            if (!isServiceRunning(context)) {
                context.startService(Intent(context, NotificationReviverService::class.java))
            }
        }
    }
}