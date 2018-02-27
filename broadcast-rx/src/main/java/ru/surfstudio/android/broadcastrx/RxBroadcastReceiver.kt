package ru.surfstudio.android.broadcastrx

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import io.reactivex.Observable

/**
 * Реактивная обёртка над BroadcastReceiver
 */
abstract class RxBroadcastReceiver<T> constructor(private val context: Context, private val intentFilter: IntentFilter) {

    fun observeBroadcast(): Observable<T> {
        return Observable.create(
                { emitter ->
                    val broadcastReceiver = object : BroadcastReceiver() {
                        override fun onReceive(context: Context, intent: Intent) {
                            parseBroadcastIntent(intent)?.let {
                                emitter.onNext(it)
                            }
                        }
                    }
                    registerBroadcastReceiver(broadcastReceiver)
                    emitter.setCancellable {
                        unregisterBroadcastReceiver(broadcastReceiver)
                    }
                })
    }

    private fun registerBroadcastReceiver(broadcastReceiver: BroadcastReceiver) {
        context.registerReceiver(broadcastReceiver, intentFilter)
    }

    private fun unregisterBroadcastReceiver(broadcastReceiver: BroadcastReceiver) {
        context.unregisterReceiver(broadcastReceiver)
    }

    abstract fun parseBroadcastIntent(intent: Intent): T?
}