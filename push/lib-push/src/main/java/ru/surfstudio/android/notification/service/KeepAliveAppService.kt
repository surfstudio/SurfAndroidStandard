package ru.surfstudio.android.notification.service

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import ru.surfstudio.android.utilktx.util.SdkUtils

/**
 * Empty service which sends broadcast to [KeepAliveAppServiceStartReceiver] for restart
 */
class KeepAliveAppService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        sendBroadcast(Intent(this, KeepAliveAppServiceStartReceiver::class.java))
    }

    companion object {

        @Suppress("DEPRECATION")
        private fun isServiceRunning(context: Context): Boolean {
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
                    ?: throw RuntimeException(ActivityManager::class.java.simpleName + " is null")
            return manager.getRunningServices(Integer.MAX_VALUE).find {
                service -> KeepAliveAppService::class.java.name == service.service.className
            } != null
        }

        fun startServiceWithCheck(context: Context) {
            if (!SdkUtils.isAtLeastOreo() && !isServiceRunning(context)) {
                context.startService(Intent(context, KeepAliveAppService::class.java))
            }
        }
    }
}