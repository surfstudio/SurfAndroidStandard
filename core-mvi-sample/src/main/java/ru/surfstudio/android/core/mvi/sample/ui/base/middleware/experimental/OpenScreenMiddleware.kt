package ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental

import android.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.OpenScreenEvent
import ru.surfstudio.android.core.mvi.event.ScreenResultEvent
import ru.surfstudio.android.core.mvi.ui.dsl.EventTransformerList
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.mvi.util.filterIsInstance
import ru.surfstudio.android.core.ui.event.result.SupportOnActivityResultRoute
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator
import ru.surfstudio.android.mvp.dialog.navigation.route.DialogRoute
import java.io.Serializable

interface OpenScreenMiddleware<T : Event> : RxMiddleware<T> {

    val activityNavigator: ActivityNavigator
    val fragmentNavigator: FragmentNavigator
    val dialogNavigator: DialogNavigator

    val observableRoutes: MutableList<Class<out SupportOnActivityResultRoute<*>>>

    fun openScreenByRoute(route: Route) {
        when (route) {
            is ActivityRoute -> {
                if (observableRoutes.contains(route::class.java)) {
                    return //если мы обрабатываем открытие OnActivityResult в другом методе - значит не трогаем его здесь
                }
                activityNavigator.start(route)
            }
            is FragmentRoute -> {
                fragmentNavigator.add(route, true, TRANSIT_FRAGMENT_OPEN)
            }

            is DialogRoute -> {
                dialogNavigator.show(route)
            }
        }
    }

    fun EventTransformerList<T>.openScreenDefault() = eventStream.openScreenDefault()

    fun Observable<T>.openScreenDefault() =
            filterIsInstance<OpenScreenEvent>().flatMap {
                openScreenByRoute(it.route)
                Observable.empty<T>()
            }

    fun <D : Serializable> Observable<T>.openScreenForResult(
            routeClass: Class<out SupportOnActivityResultRoute<D>>,
            screenResultEvent: ScreenResultEvent<D>
    ): Observable<out T> {

        observableRoutes.add(routeClass)

        val observeRoute = activityNavigator.observeResult(routeClass)
                .map {
                    screenResultEvent.apply { result = it }
                } as Observable<T>


        val openScreen = filterIsInstance<OpenScreenEvent>()
                .filter { routeClass.isInstance(it.route) }
                .flatMap {
                    val route = it.route as SupportOnActivityResultRoute<D>
                    activityNavigator.startForResult(route)
                    Observable.empty<T>()
                }

        return merge(observeRoute, openScreen)

    }

    fun <D : Serializable> EventTransformerList<T>.openScreenForResult(
            routeClass: Class<out SupportOnActivityResultRoute<D>>,
            screenResultEvent: ScreenResultEvent<D>
    ): Observable<out T> = eventStream.openScreenForResult(routeClass, screenResultEvent)
}