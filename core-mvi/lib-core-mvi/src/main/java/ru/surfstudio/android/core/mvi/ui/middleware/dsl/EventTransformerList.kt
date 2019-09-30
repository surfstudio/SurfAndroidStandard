package ru.surfstudio.android.core.mvi.ui.middleware.dsl

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.util.filterIsInstance

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
     * Реакция на событие типа [T].
     *
     * Следует использовать, когда нам не нужно трансформировать [T] в поток данных,
     * а нужно просто выполнить какое-то действие (например, для открытия экрана или отправки аналитики).
     *
     * @param mapper функция реакции.
     */
    inline fun <reified T : Event> react(
            crossinline mapper: (T) -> Unit
    ) = eventStream.filterIsInstance<T>().react(mapper)


    /**
     * Маппинг события типа [T] в другой тип, являющийся подтипом [E].
     *
     * Используется, когда нам нужно запустить событие при получении другого события,
     * например, когда при получении события ButtonClicked нужно эмитить событие OpenSelectPhotoDialog.
     *
     * @param mapper функция, преобразующая событие.
     */
    inline fun <reified T : Event> map(
            crossinline mapper: (T) -> E
    ): Observable<out E> {
        return eventStream.filterIsInstance<T>().map { mapper(it) }
    }

    /**
     * Маппинг события типа [T] в [Observable] с типом [E].
     *
     * Используется, когда нам нужно трансформировать событие в поток (аналог flatMap),
     * например, когда при получении события ButtonClicked нужно начать загрузку данных с i-слоя.
     *
     * @param mapper функция, преобразующая событие.
     */
    inline fun <reified T : Event> eventMap(
            crossinline mapper: (T) -> Observable<out E>
    ): Observable<out E> {
        return eventStream.filterIsInstance<T>().eventMap(mapper)
    }

    /**
     * Маппинг [Observable] типа [T] в [Observable] с типом [E].
     *
     * Используется, когда [eventMap] недостаточно, и нужно модификаторы от Observable,
     * например, при получении события TextChanged навесить debounce и distinctUntilChanged перед отправкой запроса.
     *
     * @param mapper функция, преобразующая событие.
     */
    inline fun <reified T : Event> streamMap(
            crossinline mapper: (Observable<T>) -> Observable<out E>
    ): Observable<out E> {
        return eventStream.filterIsInstance<T>().streamMap(mapper)
    }


    /**
     * Реакция на событие типа [T].
     *
     * Следует использовать, когда нам не нужно трансформировать [T] в поток данных,
     * а нужно просто выполнить какое-то действие (например, для открытия экрана или отправки аналитики).
     *
     * @param mapper функция реакции.
     */
    inline infix fun <reified T : Event> Observable<T>.react(
            crossinline mapper: (T) -> Unit
    ) = flatMap {
        mapper(it)
        Observable.empty<T>()
    }

    /**
     * Маппинг события типа [T] в [Observable] с типом [E].
     *
     * Используется, когда нам нужно трансформировать событие в поток (аналог flatMap),
     * например, когда при получении события ButtonClicked нужно начать загрузку данных с i-слоя.
     *
     * @param mapper функция, преобразующая событие.
     */
    inline infix fun <reified T : Event> Observable<T>.eventMap(
            crossinline mapper: (T) -> Observable<out E>
    ): Observable<out E> {
        return flatMap { mapper(it) }
    }

    /**
     * Маппинг [Observable] типа [T] в [Observable] с типом [E].
     *
     * Используется, когда [eventMap] недостаточно, и нужно модификаторы от Observable,
     * например, при получении события TextChanged навесить debounce и distinctUntilChanged перед отправкой запроса.
     *
     * @param mapper функция, преобразующая событие.
     */
    inline fun <reified T : Event> Observable<T>.streamMap(
            crossinline mapper: (Observable<T>) -> Observable<out E>
    ): Observable<out E> {
        return mapper(this)
    }
}