package ru.surfstudio.android.core.mvi.ui.dsl

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.util.filterIsInstance
import kotlin.collections.ArrayList

/**
 * Список Observable с событиями экрана, полученный в результате всех трансформаций исходного потока событий.
 *
 * @param eventStream исходный поток
 */
class EventTransformerList<E : Event>(
        val eventStream: Observable<E>
) : MutableList<Observable<E>> by ArrayList<Observable<E>>() {

    operator fun Observable<out E>.unaryPlus() {
        add(this as Observable<E>)
    }

    /**
     * Функция реакции на событие
     */
    inline fun <reified R : Event> react(
            crossinline mapper: (R) -> Unit
    ) = eventStream.filterIsInstance<R>().react(mapper)

    inline fun <reified R : Event> map(
            crossinline mapper: (R) -> E
    ): Observable<out E> {
        return eventStream.filterIsInstance<R>().map { mapper(it) }
    }

    inline fun <reified R : Event> eventMap(
            crossinline mapper: (R) -> Observable<out E>
    ): Observable<out E> {
        return eventStream.filterIsInstance<R>().eventMap(mapper)
    }

    inline fun <reified R : Event> streamMap(
            crossinline mapper: (Observable<R>) -> Observable<out E>
    ): Observable<out E> {
        return eventStream.filterIsInstance<R>().streamMap(mapper)
    }

    inline infix fun <reified R : Event> Observable<R>.react(
            crossinline mapper: (R) -> Unit
    ) = flatMap {
        mapper(it)
        Observable.empty<R>()
    }

    inline infix fun <reified R : Event> Observable<R>.eventMap(
            crossinline mapper: (R) -> Observable<out E>
    ): Observable<out E> {
        return flatMap { mapper(it) }
    }

    inline fun <reified R : Event> Observable<R>.streamMap(
            crossinline mapper: (Observable<R>) -> Observable<out E>
    ): Observable<out E> {
        return mapper(this)
    }
}