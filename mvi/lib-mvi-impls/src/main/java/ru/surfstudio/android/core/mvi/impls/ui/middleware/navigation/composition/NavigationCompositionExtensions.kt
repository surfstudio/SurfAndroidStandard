package ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition

import ru.surfstudio.android.core.ui.event.result.SupportOnActivityResultRoute
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute
import ru.surfstudio.android.mvp.dialog.navigation.route.DialogRoute
import java.io.Serializable

/**
 * Opens next screen
 *
 * @param route screen route
 */
fun <T : NavigationComposition> T.open(route: Route): T =
        this.apply { events = listOf(OpenScreen(route)) }

/**
 * Closes current activity
 */
fun <T : NavigationComposition> T.close(): T =
        this.apply { events = listOf(CloseActivity()) }

/**
 * Closes activity taskAffinity
 */
fun <T : NavigationComposition> T.closeTask(): T =
        this.apply { events = listOf(CloseTask()) }

/**
 * Closes screen with result
 *
 * @param route screen route
 * @param result screen result
 * @param isSuccess is result successful
 */
fun <T : NavigationComposition, R : Serializable> T.close(
        route: SupportOnActivityResultRoute<R>,
        result: R,
        isSuccess: Boolean = true
): T = this.apply { events = listOf(CloseWithResult(route, result, isSuccess)) }

/**
 * Closes dialog
 */
fun <T : NavigationComposition> T.close(route: DialogRoute) =
        this.apply { events = listOf(CloseDialog(route)) }

/**
 * Closes fragment
 */
fun <T : NavigationComposition> T.close(route: FragmentRoute) =
        this.apply { events = listOf(CloseFragment(route)) }

/**
 * Observes screen result event
 *
 * @param routeClass class to be observed
 */
fun <T : NavigationComposition, R : Serializable> T.observeResult(
        routeClass: Class<out SupportOnActivityResultRoute<R>>
): T = this.apply { events = listOf(ObserveResult(routeClass)) }