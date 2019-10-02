package ru.surfstudio.android.core.mvi.ui.middleware.dsl

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.ui.middleware.dsl.transformers.EventMapTransformer
import ru.surfstudio.android.core.mvi.ui.middleware.dsl.transformers.MapTransformer
import ru.surfstudio.android.core.mvi.ui.middleware.dsl.transformers.ReactTransformer
import ru.surfstudio.android.core.mvi.ui.middleware.dsl.transformers.StreamMapTransformer
import ru.surfstudio.android.core.mvi.util.filterIsInstance
import kotlin.reflect.KClass

/**
 * Список Observable с событиями экрана, полученный в результате всех трансформаций исходного потока событий.
 *
 * @param eventStream исходный поток
 */
open class EventTransformerList<E : Event>(
        val eventStream: Observable<E>
) : MutableList<Observable<E>> by ArrayList<Observable<E>>() {

    /**
     * Перегрузка унарного оператора для удобного добавления трансформации в список
     */
    operator fun Observable<out E>.unaryPlus() {
        add(this as Observable<E>)
    }

    /**
     * Добавление произвольного количества трансформаций в список
     */
    fun addAll(vararg transformations: Observable<out E>) {
        addAll(transformations.toList() as List<Observable<E>>)
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
            noinline mapper: (T) -> Unit
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
            noinline mapper: (T) -> E
    ): Observable<out E> {
        val mapTransformer = MapTransformer(mapper)
        return mapTransformer.transform(eventStream.filterIsInstance<T>())
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
            noinline mapper: (T) -> Observable<out E>
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
            noinline mapper: (Observable<T>) -> Observable<out E>
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
            noinline mapper: (T) -> Unit
    ) = ReactTransformer<T, E>(mapper).transform(this)

    /**
     * Маппинг события типа [T] в [Observable] с типом [E].
     *
     * Используется, когда нам нужно трансформировать событие в поток (аналог flatMap),
     * например, когда при получении события ButtonClicked нужно начать загрузку данных с i-слоя.
     *
     * @param mapper функция, преобразующая событие.
     */
    inline infix fun <reified T : Event> Observable<T>.eventMap(
            noinline mapper: (T) -> Observable<out E>
    ): Observable<out E> {
        return EventMapTransformer(mapper).transform(this)
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
            noinline mapper: (Observable<T>) -> Observable<out E>
    ): Observable<out E> {
        return StreamMapTransformer(mapper).transform(this)
    }

    /**
     * Реакция на событие типа [T].
     *
     * Следует использовать, когда нам не нужно трансформировать [T] в поток данных,
     * а нужно просто выполнить какое-то действие (например, для открытия экрана или отправки аналитики).
     *
     * @param mapper функция реакции.
     */
    inline infix fun <reified T : Event> KClass<T>.reactTo(
            noinline mapper: (T) -> Unit
    ): Observable<out E> {
        return eventStream.ofType(this.java).react(mapper)
    }

    /**
     * Маппинг события типа [T] в другой тип, являющийся подтипом [E].
     *
     * Используется, когда нам нужно запустить событие при получении другого события,
     * например, когда при получении события ButtonClicked нужно эмитить событие OpenSelectPhotoDialog.
     *
     * @param mapper функция, преобразующая событие.
     */
    inline infix fun <reified T : Event> KClass<T>.mapTo(
            noinline mapper: (T) -> E
    ): Observable<out E> {
        return eventStream.ofType(this.java).map(mapper)
    }

    /**
     * Маппинг события типа [T] в [Observable] с типом [E].
     *
     * Используется, когда нам нужно трансформировать событие в поток (аналог flatMap),
     * например, когда при получении события ButtonClicked нужно начать загрузку данных с i-слоя.
     *
     * @param mapper функция, преобразующая событие.
     */
    inline infix fun <reified T : Event> KClass<T>.eventMapTo(
            noinline mapper: (T) -> Observable<out E>
    ): Observable<out E> {
        return eventStream.ofType(this.java).eventMap(mapper)
    }

    /**
     * Маппинг [Observable] типа [T] в [Observable] с типом [E].
     *
     * Используется, когда [eventMap] недостаточно, и нужно модификаторы от Observable,
     * например, при получении события TextChanged навесить debounce и distinctUntilChanged перед отправкой запроса.
     *
     * @param mapper функция, преобразующая событие.
     */
    inline infix fun <reified T : Event> KClass<T>.streamMapTo(
            noinline mapper: (Observable<T>) -> Observable<out E>
    ): Observable<out E> {
        return eventStream.ofType(this.java).streamMap(mapper)
    }
}