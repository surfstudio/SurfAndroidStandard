package ru.surfstudio.android.rx.extension

import io.reactivex.Observable

/**
 * Преобразует любой элемент типа [T] в [Observable]<[T]>
 */
fun <T : Any> T.toObservable(): Observable<T> = Observable.just(this)