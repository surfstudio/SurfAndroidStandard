package ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvi.event.navigation.NavigationEvent
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.NavigationMiddlewareInterface
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.ScreenNavigator
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.close.ObserveResultEvent
import ru.surfstudio.android.core.mvi.util.filterIsInstance
import ru.surfstudio.android.rx.extension.toObservable
import java.io.Serializable

/**
 * [NavigationMiddlewareInterface] implementation.
 * It works with [NavigationEvent] and adds automatic listen for result functional.
 */
open class NavigationMiddleware(
        baseMiddlewareDependency: BaseMiddlewareDependency,
        override var screenNavigator: ScreenNavigator
) : NavigationMiddlewareInterface<NavigationEvent>,
        BaseMiddleware<NavigationEvent>(baseMiddlewareDependency) {

    override fun transform(eventStream: Observable<NavigationEvent>): Observable<out NavigationEvent> =
            merge(
                    eventStream.mapNavigationAuto(),
                    eventStream.listenForResultAuto()
            )

    private fun Observable<NavigationEvent>.listenForResultAuto(): Observable<NavigationEvent> {
        return filterIsInstance<ObserveResultEvent<Serializable>>().flatMap { event ->
            listenForResultMap(event.routeClass) {
                event.result = it
                event.toObservable()
            }
        }
    }
}