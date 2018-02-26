package ru.surfstudio.android.broadcastrx

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter

/**
 * Регистрация объектов широковещательного приёмника
 */
class BroadcastReceiverObject(private val context: Context,
                              private val intentFilter: IntentFilter) : BroadcastReceiverInterface {

    override fun registerBroadcastReceiver(broadcastReceiver: BroadcastReceiver) {
        context.registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun unregisterBroadcastReceiver(broadcastReceiver: BroadcastReceiver) {
        context.unregisterReceiver(broadcastReceiver)
    }
}