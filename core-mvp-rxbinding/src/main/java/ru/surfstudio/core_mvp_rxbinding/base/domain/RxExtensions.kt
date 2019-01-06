package ru.surfstudio.core_mvp_rxbinding.base.domain

import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer

fun <T> Relay<T>.asObservable(): Observable<T> {
    return this.hide()
}

fun <T> Relay<T>.asConsumer(): Consumer<T> {
    return this
}

fun <T, T1> Observable<T>.combine(other: Observable<T1>): Observable<Pair<T, T1>> =
        Observable.combineLatest(
                this,
                other,
                BiFunction { t: T, t1: T1 -> Pair(t, t1) }
        )

fun <T, T1, R> Observable<T>.combine(other: Observable<T1>, transformer: (T, T1) -> R): Observable<R> =
        Observable.combineLatest(
                this,
                other,
                BiFunction { t: T, t1: T1 -> transformer(t, t1) }
        )
