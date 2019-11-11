package ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition

import io.reactivex.Observable
import io.reactivex.internal.operators.observable.ObserverResourceWrapper
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.NavigationEvent
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.NavigationMiddlewareInterface
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.ScreenNavigator
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.mvi.util.filterIsInstance
import ru.surfstudio.android.core.ui.event.result.SupportOnActivityResultRoute
import ru.surfstudio.android.rx.extension.toObservable
import java.io.Serializable

/**
 * Шаблонный базовый [RxMiddleware] с поддержкой навигации.
 */
class NavigationMiddleware(
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
        return filterIsInstance<ObserveResult<*>>().flatMap { event ->
            val castedEvent = event as ObserveResult<Serializable>
            listenForResultMap(castedEvent.routeClass) {
                castedEvent.result = it
                castedEvent.toObservable()
            }
        }
    }
}