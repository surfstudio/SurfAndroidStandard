package ru.surfstudio.android.core.mvi.impls.ui.middleware.dsl.transformers.rx

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.composition.CompositionEvent
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
import java.lang.IllegalStateException

/**
 * [CompositionEvent]'s decomposition to another middleware.
 *
 * It works as follows:
 *  1) Extracts input events from [CompositionEvent].
 *  2) Passes events to another [RxMiddleware]<[T]> responsible for mapping from input to output.
 *  3) Packs output events in the [CompositionEvent] and pass it to the parent middleware.
 *
 *  If there's no input events, it can still produce output events if necessary.
 *
 *  Output events are created by calling no-args constructor,
 *  so composition event [C] must have one.
 */
class CompositionTransformer<T : Event, C : CompositionEvent<T>>(
        private val compositionEventClass: Class<C>,
        private val middleware: RxMiddleware<T>
) : ObservableTransformer<C, C> {

    override fun apply(upstream: Observable<C>): ObservableSource<C> {
        val inEvents = upstream.flatMap { composition -> Observable.fromIterable(composition.events) }
        val outEvents = middleware.transform(inEvents)
        return outEvents.map(::createCompositionEvent)
    }

    private fun createCompositionEvent(nestedEvent: T): C {
        val newCompositionEvent = try {
            compositionEventClass.newInstance()
        } catch (exception: InstantiationException) {
            throw CompositionEventNoConstructorException()
        }

        return newCompositionEvent.apply { events = listOf(nestedEvent) }
    }

    companion object {

        /**
         * Creates [CompositionTransformer].
         *
         * We use static method instead of constructor
         * to enable type erasure avoiding mechanism by Kotlin.
         */
        inline fun <T : Event, reified C : CompositionEvent<T>> create(
                middleware: RxMiddleware<T>
        ): CompositionTransformer<T, C> {
            val transformationClass = C::class.java
            return CompositionTransformer(transformationClass, middleware)
        }
    }

    class CompositionEventNoConstructorException :
            IllegalStateException("All composition events must have no args constructor!")
}