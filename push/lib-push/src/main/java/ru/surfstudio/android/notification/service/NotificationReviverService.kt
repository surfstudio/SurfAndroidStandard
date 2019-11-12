package ru.surfstudio.android.notification.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import ru.surfstudio.android.logger.Logger

/**
 * Пустой сервис, который после смерти отправляет броадкаст в [BaseNotificationProcessStarter]
 */
class NotificationReviverService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Logger.d("NotificationReviverService :: onStartCommand, flags=$flags, startId=$startId")
        return START_STICKY
    }

    override fun onDestroy() {
        Logger.d("NotificationReviverService :: onDestroy")
        val receiverClassName = NotificationProcessStarterHolder.serviceStarter.getAppStartReceiverClassName()
        var receiverClass: Class<*>? = null
        try {
            receiverClass = Class.forName(receiverClassName)
        } catch (e: ClassNotFoundException) {
            Logger.e("Receiver with class name $receiverClassName not found")
        }
        receiverClass?.let {
            Logger.d("Sending broadcast to $receiverClassName")
            sendBroadcast(Intent(this, it))
        }
    }
}