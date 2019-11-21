package ru.surfstudio.android.core.mvi.impls.ui.middleware

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.ui.middleware.FlatMapMiddleware
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware

/**
 * [BaseMiddleware] with simplified transform function:
 * Instead of using `transform(Observable<Event>): Observable<Event>`
 * it uses `flatMap(Event): Observable<Event>`,
 * which declarative uses when-expression block in style of reactor.
 */
abstract class BaseMapMiddleware<T : Event>(
        baseMiddlewareDependency: BaseMiddlewareDependency
) : BaseMiddleware<T>(baseMiddlewareDependency), FlatMapMiddleware<T>