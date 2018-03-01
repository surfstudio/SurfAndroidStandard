package ru.surfstudio.android.broadcastrx

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import io.reactivex.Notification
import io.reactivex.Observable
import ru.surfstudio.android.logger.Logger

/**
 * Реактивная обёртка над BroadcastReceiver
 */
abstract class RxBroadcastReceiver<T> constructor(private val context: Context) {

    fun observeBroadcast(): Observable<Notification<T>> {
        return Observable.create(
                { emitter ->
                    val broadcastReceiver = object : BroadcastReceiver() {
                        override fun onReceive(context: Context, intent: Intent) {
                            try {
                                emitter.onNext(Notification.createOnNext(parseBroadcastIntent(intent)))
                            } catch (throwable: Throwable) {
                                Logger.e(throwable)
                                emitter.onNext(Notification.createOnError<T>(RuntimeException(throwable)))
                            }
                        }
                    }
                    try {
                        context.registerReceiver(broadcastReceiver, intentFilter())
                    } catch (throwable: Throwable) {
                        Logger.e(throwable)
                        emitter.onNext(Notification.createOnError<T>(RuntimeException(throwable)))
                    }
                    emitter.setCancellable {
                        context.unregisterReceiver(broadcastReceiver)
                    }

                })
    }

    abstract fun parseBroadcastIntent(intent: Intent): T

    abstract fun intentFilter(): IntentFilter
}