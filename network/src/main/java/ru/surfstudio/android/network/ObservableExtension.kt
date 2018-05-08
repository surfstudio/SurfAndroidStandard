package ru.surfstudio.android.network

import io.reactivex.Observable
import io.reactivex.Single

/**
 * Extension функции для rx.reactiveio.Observable
 */

/**
 * Преобразует содержание Observable, которое наследует [Transformable]
 */
fun <T : Transformable<R>, R> Observable<T>.transform(): Observable<R> = map { it.transform() }

/**
 * Преобразует коллекцию внутри Observable, элементы которой являются [Transformable]
 */
fun <T : Collection<Transformable<R>>, R> Observable<T>.transformCollection(): Observable<List<R>> =
        map {
            it.map {
                it.transform()
            }
        }

/**
 * Преобразует содержание Single, которое наследует [Transformable]
 */
fun <T : Transformable<R>, R> Single<T>.transform(): Single<R> = map { it.transform() }

/**
 * Преобразует коллекцию внутри Single, элементы которой являются [Transformable]
 */
fun <T : Collection<Transformable<R>>, R> Single<T>.transformCollection(): Single<List<R>> =
        map {
            it.map {
                it.transform()
            }
        }

