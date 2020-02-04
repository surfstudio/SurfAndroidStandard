package ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.navigation.NavigationEvent
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.NavigationMiddlewareInterface
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.ScreenNavigator

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
            eventStream.mapNavigationAuto()
}