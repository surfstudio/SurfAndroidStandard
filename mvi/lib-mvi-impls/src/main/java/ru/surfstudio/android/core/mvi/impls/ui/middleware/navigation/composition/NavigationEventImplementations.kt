package ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition

import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.NavigationEvent
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.OpenScreenEvent
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.close.*
import ru.surfstudio.android.core.ui.event.result.SupportOnActivityResultRoute
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.core.ui.navigation.ScreenResult
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute
import ru.surfstudio.android.mvp.dialog.navigation.route.DialogRoute
import java.io.Serializable

/**
 * Реализации базовых событий навигации для композиции
 */

data class OpenScreen(override val route: Route) : OpenScreenEvent

class CloseActivity : CloseActivityEvent {
    override fun toString(): String = "CloseActivity"
}

class CloseTask : CloseTaskEvent {
    override fun toString(): String = "CloseTask"
}

data class CloseWithResult<R : Serializable>(
        override val route: SupportOnActivityResultRoute<R>,
        override val result: R,
        override val isSuccess: Boolean
) : CloseWithResultEvent<R>

data class CloseDialog(override val route: DialogRoute) : CloseDialogEvent

data class CloseFragment(override val route: FragmentRoute) : CloseFragmentEvent

data class ObserveResult<R : Serializable>(
        val routeClass: Class<out SupportOnActivityResultRoute<R>>,
        var result: ScreenResult<R>? = null
) : NavigationEvent