package ru.surfstudio.android.core.mvi.ui.middleware.builders

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware

/**
 * Простейший Middleware с логикой flatMap: трансформации из event в Observable
 */
interface FlatMapMiddleware<T : Event> : RxMiddleware<T> {

    override fun transform(eventStream: Observable<T>): Observable<out T> =
            eventStream.flatMap(::flatMap)

    fun flatMap(event: T): Observable<out T>

}
