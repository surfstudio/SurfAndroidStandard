package ru.surfstudio.android.broadcastrx

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import io.reactivex.Observable

/**
 * Реактивная обёртка над BroadcastReceiver
 */
class RxBroadcastReceiver(private val context: Context,
                          private val intentFilter: IntentFilter) {

    companion object {
        fun create(context: Context, intentFilter: IntentFilter) = RxBroadcastReceiver(context, intentFilter).createBroadcast()
    }

    fun createBroadcast(): Observable<Intent> {
        val broadcastRegister = BroadcastReceiverObject(context, intentFilter)
        return Observable.create(
                { emitter ->
                    val broadcastReceiver = object : BroadcastReceiver() {
                        override fun onReceive(context: Context, intent: Intent) {
                            emitter.onNext(intent)
                        }
                    }
                    broadcastRegister.registerBroadcastReceiver(broadcastReceiver)
                    emitter.setCancellable {
                        broadcastRegister.unregisterBroadcastReceiver(broadcastReceiver)
                    }
                })
    }
}