package com.mobium8918.app.ui.util.extensions

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.RequestEvent
import ru.surfstudio.android.core.mvi.event.factory.EventFactory
import java.util.concurrent.TimeUnit

/**
 * Конвертирует Event в debounced Event
 */
fun <E : Event, D : Event> Observable<E>.debounceEvent(
        debounceMillis: Long,
        debouncedEventFactory: EventFactory<E, D>
): Observable<D> {
    return debounce(debounceMillis, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .map { e -> debouncedEventFactory(e) }
}

/**
 * Откладывает эмит любого [RequestEvent]
 */
fun <T, E : RequestEvent<T>> Observable<E>.delayRequest(delayMillis: Long): Observable<out E> {
    return this.delayRequestInternal(delayMillis) { true }
}

/**
 * Откладывает эмит успешного [RequestEvent]
 * @param delayMillis время задержки
 */
fun <T, E : RequestEvent<T>> Observable<E>.delaySuccessRequest(delayMillis: Long): Observable<out E> {
    return this.delayRequestInternal(delayMillis) { event -> event.isSuccess }
}

/**
 * Откладывает эмит [RequestEvent] с ошибкой
 * @param delayMillis время задержки
 */
fun <T, E : RequestEvent<T>> Observable<E>.delayErrorRequest(delayMillis: Long): Observable<out E> {
    return this.delayRequestInternal(delayMillis) { event -> event.isError }
}

/**
 * Откладывает эмит [RequestEvent] с загрузкой
 * @param delayMillis время задержки
 */
fun <T, E : RequestEvent<T>> Observable<E>.delayLoadingRequest(delayMillis: Long): Observable<out E> {
    return this.delayRequestInternal(delayMillis) { event -> event.isLoading }
}

/**
 * Откладывает эмит [RequestEvent]
 */
private fun <T, E : RequestEvent<T>> Observable<E>.delayRequestInternal(delayMillis: Long, condition: (E) -> Boolean): Observable<out E> {
    return this.compose { upStream ->
        upStream.flatMap { upStreamEvent ->
            if (condition(upStreamEvent)) {
                Observable.just(upStreamEvent).delay(delayMillis, TimeUnit.MILLISECONDS)
            } else {
                Observable.just(upStreamEvent)
            }
        }
    }
}
