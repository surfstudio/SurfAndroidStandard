package ru.surfstudio.android.notification.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Пустой сервис, который после смерти отправляет броадкаст в [AppStartReceiver]
 */
class NotificationReviverService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        val receiverClassName = NotificationProcessStarterHolder.serviceStarter.getAppStartReceiverClassName()
        var receiverClass: Class<*>? = null
        try {
            receiverClass = Class.forName(receiverClassName)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        receiverClass?.let {
            val intent = Intent(this, it)
            sendBroadcast(intent)
        }
    }
}