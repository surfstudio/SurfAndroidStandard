package ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.close.*
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.open.OpenScreenEvent
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.ui.event.result.SupportOnActivityResultRoute
import java.io.Serializable

/**
 * Интерфейс для middleware с поддержкой навигации.
 *
 * Обрабатывает события [OpenScreenEvent] и [CloseScreenEvent], определяя нужные навигаторы по классам
 *
 * Является экспериментальной фичей и не рекомендуется для использования на стабильных проектах.
 */
interface NavigationMiddlewareInterface<T : Event> : RxMiddleware<T> {

    var screenNavigator: ScreenNavigator

    /**
     * Opens the screen when [OpenScreenEvent] appears on eventStream.
     */
    fun openScreenByEvent(event: OpenScreenEvent) {
        screenNavigator.open(event.route)
    }

    /**
     * Closes the screen when [CloseScreenEvent] appears on eventStream.
     */
    fun closeScreenByEvent(event: CloseScreenEvent) {
        when (event) {
            is CloseActivityEvent ->
                screenNavigator.close()

            is CloseTaskEvent ->
                screenNavigator.closeTask()

            is CloseWithResultEvent<*> -> {
                val resultRoute = event.route as SupportOnActivityResultRoute<Serializable>
                screenNavigator.closeWithResult(resultRoute, event.result, event.isSuccess)
            }

            is CloseFragmentEvent -> {
                screenNavigator.close(event.route)
            }

            is CloseDialogEvent -> {
                screenNavigator.close(event.route)
            }
        }
    }

    /**
     * Automatically opens/closes screen when [CloseScreenEvent] or [OpenScreenEvent]
     * appears in eventStream.
     */
    fun Observable<T>.mapNavigationAuto() =
            flatMap { event ->
                when (event) {
                    is OpenScreenEvent -> openScreenByEvent(event)
                    is CloseScreenEvent -> closeScreenByEvent(event)
                }
                Observable.empty<T>()
            }
}