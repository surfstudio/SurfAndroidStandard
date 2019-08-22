package ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental

import android.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE
import android.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.navigation.OpenScreenEvent
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.navigation.ScreenResultEvent
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.navigation.close.*
import ru.surfstudio.android.core.mvi.ui.dsl.EventTransformerList
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.mvi.util.filterIsInstance
import ru.surfstudio.android.core.ui.event.result.SupportOnActivityResultRoute
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator
import ru.surfstudio.android.mvp.dialog.navigation.route.DialogRoute
import java.io.Serializable

interface NavigatorMiddleware<T : Event> : RxMiddleware<T> {

    val activityNavigator: ActivityNavigator
    val fragmentNavigator: FragmentNavigator
    val dialogNavigator: DialogNavigator

    val observableRoutes: MutableList<Class<out SupportOnActivityResultRoute<*>>>

    fun openScreenByEvent(event: OpenScreenEvent) {
        when (val route = event.route) {
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
                openScreenByEvent(it)
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
                    activityNavigator.startForResult(route).skip<T>()
                }

        return merge(observeRoute, openScreen)
    }

    fun <D : Serializable> EventTransformerList<T>.openScreenForResult(
            routeClass: Class<out SupportOnActivityResultRoute<D>>,
            screenResultEvent: ScreenResultEvent<D>
    ): Observable<out T> = eventStream.openScreenForResult(routeClass, screenResultEvent)

    fun Observable<T>.closeScreenDefault() = filterIsInstance<CloseScreenEvent>()
            .flatMap { closeScreen(it).skip<T>() }

    fun Observable<T>.mapNavigationDefault() =
            merge(openScreenDefault(), closeScreenDefault())

    fun EventTransformerList<T>.mapNavigationDefault() = eventStream.mapNavigationDefault()

    fun closeScreen(event: CloseScreenEvent) {
        when (event) {
            is CloseActivityEvent ->
                activityNavigator.finishCurrent()
            is CloseWithResultEvent<*> -> {
                val resultRoute = event.route as SupportOnActivityResultRoute<Serializable>
                activityNavigator.finishWithResult(resultRoute, event.result, event.isSuccess)
            }

            is CloseFragmentEvent -> {
                fragmentNavigator.remove(event.route, TRANSIT_FRAGMENT_CLOSE)
            }

            is CloseDialogEvent -> {
                dialogNavigator.dismiss(event.route)
            }
        }
    }
}