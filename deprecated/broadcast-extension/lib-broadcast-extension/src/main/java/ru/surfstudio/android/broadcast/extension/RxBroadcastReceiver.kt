/*
  Copyright (c) 2018-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.broadcast.extension

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
        return Observable.create { emitter ->
            val broadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    try {
                        val data = parseBroadcastIntent(intent)
                        data?.let {
                            emitter.onNext(it)
                        }
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
        }
    }

    protected abstract fun parseBroadcastIntent(intent: Intent): T?

    protected abstract fun intentFilter(): IntentFilter
}