package ru.surfstudio.standard.f_debug.network

import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

/**
 * Интерсептор, добавляющий запросам задержку.
 */
class DelayInterceptor(private val requestDelayMs: Long) : Interceptor {

    constructor(requestDelayCallback: () -> Long) : this(requestDelayCallback())

    override fun intercept(chain: Interceptor.Chain): Response {
        TimeUnit.MILLISECONDS.sleep(requestDelayMs)
        return chain.proceed(chain.request())
    }
}
