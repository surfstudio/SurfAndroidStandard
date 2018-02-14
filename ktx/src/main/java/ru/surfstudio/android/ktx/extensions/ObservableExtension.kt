package ru.surfstudio.android.ktx.extensions

import io.reactivex.Observable
import ru.surfstudio.android.core.util.Transformable

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
