package ru.surfstudio.android.network

import io.reactivex.Observable

/**
 * Extension функции для rx.reactiveio.Observable
 */

fun <T : Transformable<R>, R> Observable<T>.transform(): Observable<R> = map { it.transform() }

fun <T : Collection<Transformable<R>>, R> Observable<T>.transformCollection(): Observable<List<R>> =
        map {
            it.map {
                it.transform()
            }
        }
