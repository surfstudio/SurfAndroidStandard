package ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation

import android.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE
import android.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import io.reactivex.Observable
import ru.surfstudio.android.core.ui.event.result.SupportOnActivityResultRoute
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.core.ui.navigation.ScreenResult
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator
import ru.surfstudio.android.mvp.dialog.navigation.route.DialogRoute
import java.io.Serializable

/**
 * Screen Navigator used to simplify navigation process.
 */
open class ScreenNavigator(
        protected val activityNavigator: ActivityNavigator,
        protected val fragmentNavigator: FragmentNavigator,
        protected val dialogNavigator: DialogNavigator
) {

    /**
     * Open screen with [Route]
     */
    open fun open(route: Route) {
        when (route) {
            is ActivityRoute -> openActivity(route)
            is DialogRoute -> openDialog(route)
            is FragmentRoute -> openFragment(route)
        }
    }

    /**
     * Open screen with [Route] for result
     */
    open fun openForResult(route: SupportOnActivityResultRoute<*>) {
        openActivityForResult(route)
    }

    /**
     * Close current activity
     */
    open fun close() {
        activityNavigator.finishCurrent()
    }

    /**
     * Close activity taskAffinity
     */
    open fun closeTask() {
        activityNavigator.finishAffinity()
    }

    /**
     * Close activity with result
     */
    open fun <R : Serializable> closeWithResult(
            route: SupportOnActivityResultRoute<R>,
            result: R,
            isSuccess: Boolean
    ) {
        activityNavigator.finishWithResult(route, result, isSuccess)
    }

    /**
     * Close Dialog with specific [Route]
     */
    open fun close(route: DialogRoute) {
        dialogNavigator.dismiss(route)
    }

    /**
     * Close Fragment with specific  [Route]
     */
    open fun close(route: FragmentRoute) {
        fragmentNavigator.hide(route, TRANSIT_FRAGMENT_CLOSE)
    }

    /**
     * Observe screen result
     *
     * Should be called strictly before opening screen with [Route] of type [routeClass].
     */
    open fun <R : Serializable> observeResult(
            routeClass: Class<out SupportOnActivityResultRoute<R>>
    ): Observable<ScreenResult<R>> {
        return activityNavigator.observeResult(routeClass)
    }

    protected open fun openActivityForResult(route: SupportOnActivityResultRoute<*>) {
        activityNavigator.startForResult(route)
    }

    protected open fun openActivity(route: ActivityRoute) {
        activityNavigator.start(route)
    }

    protected open fun openDialog(route: DialogRoute) {
        dialogNavigator.show(route)
    }

    protected open fun openFragment(route: FragmentRoute) {
        fragmentNavigator.show(route, TRANSIT_FRAGMENT_OPEN)
    }
}