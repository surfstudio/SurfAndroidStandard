package ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.close

import ru.surfstudio.android.core.mvi.event.navigation.NavigationEvent
import ru.surfstudio.android.core.ui.event.result.SupportOnActivityResultRoute
import ru.surfstudio.android.core.ui.navigation.ScreenResult
import java.io.Serializable

/**
 * Event that observes screenResult
 *
 * @property routeClass class to be observed
 * @property result screen result
 */
interface ObserveResultEvent<R : Serializable> : NavigationEvent {
    val routeClass: Class<out SupportOnActivityResultRoute<R>>
    var result: ScreenResult<R>?
}