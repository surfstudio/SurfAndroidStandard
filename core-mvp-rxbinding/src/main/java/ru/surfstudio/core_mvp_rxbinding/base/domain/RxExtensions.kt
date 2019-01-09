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

/**
 * Extension для комбинирования двух [Observable] в пару [Pair] с типами [T], [T1]
 *
 * @param other [Observable] типа [T1] для комбинирования с исходным
 *
 * @return [Observable] с результирующей парой
 */
fun <T, T1> Observable<T>.combine(other: Observable<T1>): Observable<Pair<T, T1>> =
        Observable.combineLatest(
                this,
                other,
                BiFunction { t: T, t1: T1 -> Pair(t, t1) }
        )

/**
 * Extension для комбинирования двух [Observable] в какой-либо результат типа [R]
 *
 * @param other [Observable] типа [T1] для комбинирования с исходным
 * @param transformer функция-трансформер, преобразующая параметры типа [T] и [T1] в результат типа [R]
 */
fun <T, T1, R> Observable<T>.combine(other: Observable<T1>, transformer: (T, T1) -> R): Observable<R> =
        Observable.combineLatest(
                this,
                other,
                BiFunction { t: T, t1: T1 -> transformer(t, t1) }
        )
