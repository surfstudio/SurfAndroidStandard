package ru.surfstudio.android.broadcastrx

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import io.reactivex.Observable
import ru.surfstudio.android.logger.Logger

/**
 * Реактивная обёртка над BroadcastReceiver
 */
abstract class RxBroadcastReceiver<T> constructor(private val context: Context) {

    /**
     * Подписка на получение результата broadcast
     *
     * @return Observable с результатом типа подписки
     */
    fun observeBroadcast(): Observable<T> {
        return Observable.create(
                { emitter ->
                    val broadcastReceiver = object : BroadcastReceiver() {
                        override fun onReceive(context: Context, intent: Intent) {
                            try {
                                val data = parseBroadcastIntent(intent)
                                if (data != null) emitter.onNext(data)
                            } catch (throwable: Throwable) {
                                Logger.e(throwable)
                                emitter.onError(throwable)
                            }
                        }
                    }
                    try {
                        context.registerReceiver(broadcastReceiver, intentFilter())
                    } catch (throwable: Throwable) {
                        Logger.e(throwable)
                        emitter.onError(throwable)
                    }
                    emitter.setCancellable {
                        context.unregisterReceiver(broadcastReceiver)
                    }

                })
    }

    protected abstract fun parseBroadcastIntent(intent: Intent): T?

    protected abstract fun intentFilter(): IntentFilter
}