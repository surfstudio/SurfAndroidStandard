package ru.surfstudio.android.core.mvi.ui.middleware

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.RequestEvent
import ru.surfstudio.android.core.mvi.event.factory.EventFactory
import ru.surfstudio.android.core.mvi.ui.relation.StateEmitter
import ru.surfstudio.android.core.mvp.binding.rx.request.Request
import ru.surfstudio.android.core.mvp.binding.rx.request.extension.asRequest

/**
 * [Middleware] с реализацией в Rx.
 *
 * Кроме типизации по [Observable], содержит некоторые вспомогательные функции для работы с потоком.
 */
interface RxMiddleware<T : Event> : Middleware<T, Observable<T>, Observable<out T>>, StateEmitter {

    /**
     * Расширение для Observable, переводящее асинхронный запрос загрузки данных к Observable<[RequestEvent]>.
     *
     * При добавлении к цепочке observable, необходимо применять именно к тому элементу, который будет эмитить значения.
     */
    fun <T, E : RequestEvent<T>> Observable<T>.asRequestEvent(
            eventFactory: EventFactory<Request<T>, E>
    ): Observable<out E> = this
            .asRequest()
            .map { request -> eventFactory(request) }

    /**
     * Расширение для Single, переводящее асинхронный запрос загрузки данных к Observable<[RequestEvent]>.
     *
     * При добавлении к цепочке observable, необходимо применять именно к тому элементу, который будет эмитить значения.
     */
    fun <T, E : RequestEvent<T>> Single<T>.asRequestEvent(
            eventFactory: EventFactory<Request<T>, E>
    ): Observable<out E> = this
            .asRequest()
            .map { request -> eventFactory(request) }

    /**
     * Расширение для Completable, переводящее асинхронный запрос загрузки данных к Observable<[RequestEvent]>.
     *
     * При добавлении к цепочке observable, необходимо применять именно к тому элементу, который будет эмитить значения.
     */
    fun <E : RequestEvent<Unit>> Completable.asRequestEvent(
            eventFactory: EventFactory<Request<Unit>, E>
    ): Observable<out E> = this
            .asRequest()
            .map { request -> eventFactory(request) }

    /**
     * Расширение для Flowable, переводящее асинхронный запрос загрузки данных к Observable<[RequestEvent]>.
     *
     * При добавлении к цепочке observable, необходимо применять именно к тому элементу, который будет эмитить значения.
     */
    fun <T, E : RequestEvent<T>> Flowable<T>.asRequestEvent(
            eventFactory: EventFactory<Request<T>, E>
    ): Observable<out E> = this
            .asRequest()
            .map { request -> eventFactory(request) }

    /**
     * Пропуск дальнейшего проброса события.
     */
    fun <T> skip(): Observable<T> = Observable.empty<T>()

    /**
     * Выполнение действия и пропуск дальнейшего проброса события.
     */
    fun <T> doAndSkip(action: () -> Unit): Observable<T> {
        action()
        return Observable.empty<T>()
    }

    /**
     * @see Observable.merge
     */
    fun merge(vararg observables: Observable<out T>): Observable<out T> = Observable.merge(observables.toList())

    /**
     * @see skip
     */
    fun <T> Any?.skip(): Observable<T> = Observable.empty<T>()
}