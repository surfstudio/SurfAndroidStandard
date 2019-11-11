package ru.surfstudio.android.core.mvi.impls.ui.middleware.dsl.transformers.rx

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.composition.CompositionEvent
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware

/**
 * Декомпозиция событий с последующей обработкой в стороннем middleware.
 *
 * Механизм следующий:
 *  1) Извлечение входных событий из базового события [CompositionEvent].
 *  2) Направление этих событий в сторонний [RxMiddleware] и их трансформация в выходные события
 *  3) Помещение выходных событий в базовое событие [CompositionEvent] для дальнейшей обработки
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