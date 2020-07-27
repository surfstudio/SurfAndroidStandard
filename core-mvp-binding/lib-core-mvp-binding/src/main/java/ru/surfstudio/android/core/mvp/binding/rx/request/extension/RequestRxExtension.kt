package ru.surfstudio.android.core.mvp.binding.rx.request.extension

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import ru.surfstudio.android.core.mvp.binding.rx.request.Request

/**
 * Расширение для Observable, переводящее асинхронный запрос загрузки данных к Observable<[Request]>.
 *
 * При добавлении к цепочке observable, необходимо применять именно к тому элементу, который будет эмитить значения.
 */
fun <T> Observable<T>.asRequest(): Observable<Request<T>> = this
        .map { Request.Success(it) as Request<T> }
        .startWith(Request.Loading())
        .onErrorReturn { t: Throwable -> Request.Error(t) }

/**
 * Расширение для Single, переводящее асинхронный запрос загрузки данных к Observable<[Request]>.
 *
 * При добавлении к цепочке observable, необходимо применять именно к тому элементу, который будет эмитить значения.
 */
fun <T> Single<T>.asRequest(): Observable<Request<T>> = this.toObservable()
        .map { Request.Success(it) as Request<T> }
        .startWith(Request.Loading())
        .onErrorReturn { t: Throwable -> Request.Error(t) }

/**
 * Расширение для Completable, переводящее асинхронный запрос загрузки данных к Observable<[Request]>.
 *
 * При добавлении к цепочке observable, необходимо применять именно к тому элементу, который будет эмитить значения.
 */
fun Completable.asRequest(): Observable<Request<Unit>> = this.toSingleDefault(Unit)
        .toObservable()
        .map { Request.Success(Unit) as Request<Unit> }
        .startWith(Request.Loading())
        .onErrorReturn { t: Throwable -> Request.Error(t) }


/**
 * Расширение для Flowable, переводящее асинхронный запрос загрузки данных к Observable<[Request]>.
 *
 * При добавлении к цепочке observable, необходимо применять именно к тому элементу, который будет эмитить значения.
 */
fun <T> Flowable<T>.asRequest(): Observable<Request<T>> = this.toObservable()
        .map { Request.Success(it) as Request<T> }
        .startWith(Request.Loading())
        .onErrorReturn { t: Throwable -> Request.Error(t) }
