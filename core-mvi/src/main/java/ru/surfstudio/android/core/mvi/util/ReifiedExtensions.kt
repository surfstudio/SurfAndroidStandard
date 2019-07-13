package ru.surfstudio.android.core.mvi.util

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.hub.RxEventHub

inline fun <reified R> Observable<*>.filterIsInstance(): Observable<R> = this
        .filter { it is R }
        .map { it as R }

inline fun <reified R> RxEventHub<*>.observeOnly(): Observable<R> = observe()
        .filterIsInstance()