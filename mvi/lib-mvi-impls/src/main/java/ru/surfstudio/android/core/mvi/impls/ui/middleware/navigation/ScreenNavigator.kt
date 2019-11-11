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
 * Навигатор
 */
open class ScreenNavigator(
        protected val activityNavigator: ActivityNavigator,
        protected val fragmentNavigator: FragmentNavigator,
        protected val dialogNavigator: DialogNavigator
) {

    /**
     * Открытие экрана с [Route]
     */
    fun open(route: Route) {
        when (route) {
            is SupportOnActivityResultRoute<*> -> openForResult(route)
            is ActivityRoute -> openActivity(route)
            is DialogRoute -> openDialog(route)
            is FragmentRoute -> openFragment(route)
        }
    }

    /**
     * Закрытие activity
     */
    open fun close() {
        activityNavigator.finishCurrent()
    }

    /**
     * Закрытие таска activity
     */
    open fun closeTask() {
        activityNavigator.finishAffinity()
    }

    /**
     * Закрытие activity c простановкой результата
     */
    open fun <R : Serializable> closeWithResult(
            route: SupportOnActivityResultRoute<R>,
            result: R,
            isSuccess: Boolean
    ) {
        activityNavigator.finishWithResult(route, result, isSuccess)
    }

    /**
     * Закрытие Dialog
     */
    open fun close(route: DialogRoute) {
        dialogNavigator.dismiss(route)
    }

    /**
     * Закрытие Fragment
     */
    open fun close(route: FragmentRoute) {
        fragmentNavigator.hide(route, TRANSIT_FRAGMENT_CLOSE)
    }

    /**
     * Подписка на результат работы экрана
     *
     * Следует выполнять строго до отрытия экрана с помощью [Route] типа [routeClass].
     */
    open fun <R : Serializable> observeResult(
            routeClass: Class<out SupportOnActivityResultRoute<R>>
    ): Observable<ScreenResult<R>> {
        return activityNavigator.observeResult(routeClass)
    }

    protected open fun openForResult(route: SupportOnActivityResultRoute<*>) {
        activityNavigator.startForResult(route)
    }

    protected open fun openActivity(route: ActivityRoute) {
        activityNavigator.start(route)
    }

    protected open fun openDialog(route: DialogRoute) {
        dialogNavigator.show(route)
    }

    protected open fun openFragment(route: FragmentRoute) {
        fragmentNavigator.add(route, true, TRANSIT_FRAGMENT_OPEN)
    }
}