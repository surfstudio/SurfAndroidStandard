package ru.surfstudio.android.core.mvi.impls.ui.middleware.dsl.transformers.rx

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.composition.CompositionEvent
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware

/**
 * [CompositionEvent]'s decomposition to another middleware.
 *
 * It works as follows:
 *  1) Extracts input events from [CompositionEvent].
 *  2) Passes events to another [RxMiddleware]<[T]> responsible for mapping from input to output.
 *  3) Packs output events in the [CompositionEvent] and pass it to the parent middleware.
 */
class CompositionTransformer<T: Event, C: CompositionEvent<T>>(
        private val middleware: RxMiddleware<T>
): ObservableTransformer<C, C> {

    override fun apply(upstream: Observable<C>): ObservableSource<C> {
        return upstream.flatMap { composition ->
            val inEvents = Observable.fromIterable(composition.events)
            val outEvents = middleware.transform(inEvents)
            outEvents.map { composition.apply { events = listOf(it) } }
        }
    }
}