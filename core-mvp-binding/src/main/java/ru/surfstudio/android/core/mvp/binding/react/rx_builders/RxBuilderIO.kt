package ru.surfstudio.android.core.mvp.binding.react.rx_builders

import io.reactivex.*
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider

interface RxBuilderIO {

    val schedulersProvider: SchedulersProvider

    /**
     * Build-функция, переводящая [Single] в поток из Schedulers.io()
     */
    fun <T> Single<T>.io(): Single<T> =
            subscribeOn(schedulersProvider.worker())

    /**
     * Build-функция, переводящая [Observable] в поток из Schedulers.io()
     */
    fun <T> Observable<T>.io(): Observable<T> =
            subscribeOn(schedulersProvider.worker())

    /**
     * Build-функция, переводящая [Observable] в поток из Schedulers.io()
     */
    fun <T> Maybe<T>.io(): Maybe<T> =
            subscribeOn(schedulersProvider.worker())

    /**
     * Build-функция, [Completable] в поток из Schedulers.io()
     */
    fun Completable.io(): Completable =
            subscribeOn(schedulersProvider.worker())

}