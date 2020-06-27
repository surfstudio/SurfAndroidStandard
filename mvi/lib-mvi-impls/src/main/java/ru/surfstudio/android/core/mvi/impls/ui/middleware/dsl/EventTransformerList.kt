/*
  Copyright (c) 2020, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
package ru.surfstudio.android.core.mvi.impls.ui.middleware.dsl

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.composition.CompositionEvent
import ru.surfstudio.android.core.mvi.event.composition.CompositionListEvent
import ru.surfstudio.android.core.mvi.impls.ui.middleware.dsl.transformers.rx.*
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.mvi.util.filterIsInstance
import kotlin.reflect.KClass

/**
 * List with all transformations of the screen events.
 *
 * @param eventStream source stream with screen events
 */
open class EventTransformerList<E : Event>(
        val eventStream: Observable<E>
) : MutableList<Observable<E>> by ArrayList<Observable<E>>() {

    /**
     * Unary operator overload to add Observables directly to the list
     */
    operator fun Observable<out E>.unaryPlus() {
        add(this as Observable<E>)
    }

    /**
     * Add several transformations to the list
     */
    fun addAll(vararg transformations: Observable<out E>) {
        addAll(transformations.toList() as List<Observable<E>>)
    }


    /**
     * Reaction on event of type [T].
     *
     * You should use it when the event [T] shouldn't be passed through data stream,
     * and we need to simply react on the event (for example, to open a screen, log something, or to send analytics).
     *
     * @param mapper функция реакции.
     */
    inline infix fun <reified T : Event> Observable<T>.react(
            noinline mapper: (T) -> Unit
    ) = compose(ReactTransformer<T, E>(mapper))

    /**
     * Reaction on event of type [T].
     *
     * You should use it when the event [T] shouldn't be passed through data stream,
     * and we need to simply react on the event (for example, to open a screen, log something, or to send analytics).
     *
     * @param mapper функция реакции.
     */
    inline fun <reified T : Event> react(
            noinline mapper: (T) -> Unit
    ) = eventStream.filterIsInstance<T>().react(mapper)

    /**
     * Reaction on event of type [T].
     *
     * You should use it when the event [T] shouldn't be passed through data stream,
     * and we need to simply react on the event (for example, to open a screen, log something, or to send analytics).
     *
     * @param mapper функция реакции.
     */
    inline infix fun <reified T : Event> KClass<T>.reactTo(
            noinline mapper: (T) -> Unit
    ): Observable<out E> {
        return eventStream.ofType(this.java).react(mapper)
    }

    /**
     * Maps events of type [T] in another type, successor of type [E].
     *
     * Used when we need to map event to another event,
     * For example, when the PhotoButtonClick is appeared, we need to emit OpenSelectPhotoDialog.
     *
     * @param mapper mapper function.
     */
    inline infix fun <reified T : Event> Observable<T>.map(
            noinline mapper: (T) -> E
    ): Observable<out E> = compose(MapTransformer(mapper))

    /**
     * Maps events of type [T] in another type, successor of type [E].
     *
     * Used when we need to map event to another event,
     * For example, when the PhotoButtonClick is appeared, we need to emit OpenSelectPhotoDialog.
     *
     * @param mapper mapper function.
     */
    inline fun <reified T : Event> map(
            noinline mapper: (T) -> E
    ): Observable<out E> {
        return eventStream.filterIsInstance<T>() map mapper
    }

    /**
     * Maps events of type [T] in another type, successor of type [E].
     *
     * Used when we need to map event to another event,
     * For example, when the PhotoButtonClick is appeared, we need to emit OpenSelectPhotoDialog.
     *
     * @param mapper mapper function.
     */
    inline infix fun <reified T : Event> KClass<T>.mapTo(
            noinline mapper: (T) -> E
    ): Observable<out E> {
        return eventStream.ofType(this.java).map(mapper)
    }

    /**
     * Maps events of type [T] to [Observable]<[E]>.
     *
     * Can be used when we need to transform event to a stream (like [flatMap]),
     * For example, when we should load data from data-layer on ButtonClicked event.
     *
     * @param mapper mapper function.
     */
    infix fun <T : Event> Observable<T>.eventMap(
            mapper: (T) -> Observable<out E>
    ): Observable<out E> {
        return compose(EventMapTransformer(mapper))
    }

    /**
     * Maps events of type [T] to [Observable]<[E]>.
     *
     * Can be used when we need to transform event to a stream (like [flatMap]),
     * For example, when we should load data from data-layer on ButtonClicked event.
     *
     * @param mapper mapper function.
     */
    inline fun <reified T : Event> eventMap(
            noinline mapper: (T) -> Observable<out E>
    ): Observable<out E> {
        return eventStream.filterIsInstance<T>().eventMap(mapper)
    }

    /**
     * Maps events of type [T] to [Observable]<[E]>.
     *
     * Can be used when we need to transform event to a stream (like [flatMap]),
     * For example, when we should load data from data-layer on ButtonClicked event.
     *
     * @param mapper mapper function.
     */
    inline infix fun <reified T : Event> KClass<T>.eventMapTo(
            noinline mapper: (T) -> Observable<out E>
    ): Observable<out E> {
        return eventStream.ofType(this.java).eventMap(mapper)
    }

    /**
     * Maps [Observable]<[T]> to [Observable]<[E]>.
     *
     * Can be used when the [eventMap] is not enough, and we should modify source [Observable].
     * For example, when we need to add debounce and distinctUntilChanged on TextChanged event before sending it to network.
     *
     * @param mapper mapper function.
     */
    fun <T : Event> Observable<T>.streamMap(
            mapper: (Observable<T>) -> Observable<out E>
    ): Observable<out E> {
        return compose(StreamMapTransformer(mapper))
    }

    /**
     * Maps [Observable]<[T]> to [Observable]<[E]>.
     *
     * Can be used when the [eventMap] is not enough, and we should modify source [Observable].
     * For example, when we need to add debounce and distinctUntilChanged on TextChanged event before sending it to network.
     *
     * @param mapper mapper function.
     */
    inline fun <reified T : Event> streamMap(
            noinline mapper: (Observable<T>) -> Observable<out E>
    ): Observable<out E> {
        return eventStream.filterIsInstance<T>().streamMap(mapper)
    }

    /**
     * Maps [Observable]<[T]> to [Observable]<[E]>.
     *
     * Can be used when the [eventMap] is not enough, and we should modify source [Observable].
     * For example, when we need to add debounce and distinctUntilChanged on TextChanged event before sending it to network.
     *
     * @param mapper mapper function.
     */
    inline infix fun <reified T : Event> KClass<T>.streamMapTo(
            noinline mapper: (Observable<T>) -> Observable<out E>
    ): Observable<out E> {
        return eventStream.ofType(this.java).streamMap(mapper)
    }


    /**
     * Decompose events to process them in another middleware
     *
     * @see [CompositionTransformer]
     */
    inline infix fun <T : Event, reified C : CompositionEvent<T>> Observable<C>.decompose(
            mw: RxMiddleware<T>
    ): Observable<C> {
        return compose(CompositionTransformer.create<T, C>(mw))
    }

    /**
     * Decompose events to process them in another middleware
     *
     * @see [SingleCompositionTransformer]
     */
    inline infix fun <T : Event, reified C : CompositionListEvent<T>> Observable<C>.unwrap(
            mw: RxMiddleware<T>
    ): Observable<C> {
        return compose(SingleCompositionTransformer.create<T, C>(mw))
    }

    /**
     * Decompose events filtered by type [T], to process them in another middleware.
     *
     * @see [CompositionTransformer]
     */
    inline fun <reified T : Event, reified C : CompositionEvent<T>> decomposeTo(mw: RxMiddleware<T>) =
            eventStream.filterIsInstance<C>().decompose(mw)

    /**
     * Decompose events filtered by type [T], to process them in another middleware.
     *
     * @see [CompositionTransformer]
     */
    inline fun <reified T : Event, reified C : CompositionListEvent<T>> unwrapTo(mw: RxMiddleware<T>) =
            eventStream.filterIsInstance<C>().unwrap(mw)

    /**
     * Decompose events filtered by type [T], to process them in another middleware.
     *
     * Used in DSL mechanism with syntax: "Event::class decomposeTo middleware"
     *
     * @see [CompositionTransformer]
     */
    inline infix fun <T : Event, reified C : CompositionEvent<T>> KClass<C>.decomposeTo(mw: RxMiddleware<T>) =
            eventStream.ofType(this.java).decompose(mw)


    /**
     * Decompose events filtered by type [T], to process them in another middleware.
     *
     * Used in DSL mechanism with syntax: "Event::class decomposeTo middleware"
     *
     * @see [CompositionTransformer]
     */
    inline infix fun <T : Event, reified C : CompositionListEvent<T>> KClass<C>.unwrapTo(mw: RxMiddleware<T>) =
            eventStream.ofType(this.java).unwrap(mw)

    /**
     * Filters events by a given [filterCondition].
     */
    infix fun <T : Event> KClass<T>.filter(filterCondition: (T) -> Boolean): Observable<T> =
            eventStream.ofType(this.java).filter(filterCondition)
}