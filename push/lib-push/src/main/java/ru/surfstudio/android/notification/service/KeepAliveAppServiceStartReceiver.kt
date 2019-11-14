package ru.surfstudio.android.notification.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Receives intent from [KeepAliveAppService]
 * and on first boot; need to restart service
 * for keeping app alive in background (for example, receiving pushes)
 */
class KeepAliveAppServiceStartReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            KeepAliveAppService.startServiceWithCheck(it)
        }
    }
}