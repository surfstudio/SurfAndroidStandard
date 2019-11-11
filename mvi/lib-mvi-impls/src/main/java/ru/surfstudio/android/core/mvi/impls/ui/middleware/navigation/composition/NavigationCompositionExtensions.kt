package ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition

import ru.surfstudio.android.core.ui.event.result.SupportOnActivityResultRoute
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute
import ru.surfstudio.android.mvp.dialog.navigation.route.DialogRoute
import java.io.Serializable

fun <T : NavigationComposition> T.open(route: Route): T =
        this.apply { events = listOf(OpenScreen(route)) }

fun <T : NavigationComposition> T.close(): T =
        this.apply { events = listOf(CloseActivity()) }


fun <T : NavigationComposition> T.closeTask(): T =
        this.apply { events = listOf(CloseTask()) }

fun <T : NavigationComposition, R : Serializable> T.close(
        route: SupportOnActivityResultRoute<R>,
        result: R,
        isSuccess: Boolean = true
): T = this.apply { events = listOf(CloseWithResult(route, result, isSuccess)) }

fun <T : NavigationComposition> T.close(route: DialogRoute) =
        this.apply { events = listOf(CloseDialog(route)) }

fun <T : NavigationComposition> T.close(route: FragmentRoute) =
        this.apply { events = listOf(CloseFragment(route)) }

fun <T : NavigationComposition, R : Serializable> T.observeResult(
        routeClass: Class<out SupportOnActivityResultRoute<R>>
): T = this.apply { events = listOf(ObserveResult(routeClass)) }