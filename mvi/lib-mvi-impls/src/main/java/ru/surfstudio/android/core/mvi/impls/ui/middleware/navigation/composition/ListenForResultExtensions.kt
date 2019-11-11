package ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.factory.EventFactory
import ru.surfstudio.android.core.mvi.impls.ui.middleware.dsl.EventTransformerList
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.NavigationEvent
import ru.surfstudio.android.core.ui.event.result.SupportOnActivityResultRoute
import ru.surfstudio.android.core.ui.navigation.ScreenResult
import ru.surfstudio.android.rx.extension.toObservable
import java.io.Serializable

/**
 * TODO добавить документацию
 */
fun <T : Event, R : Serializable> EventTransformerList<T>.listenForResultMap(
        routeClass: Class<out SupportOnActivityResultRoute<R>>,
        navigationEventFactory: EventFactory<List<NavigationEvent>, NavigationComposition>,
        onScreenResult: (ScreenResult<R>) -> Observable<T>
): Observable<T> {
    val event = navigationEventFactory.invoke(listOf(ObserveResult(routeClass)))
    val observeResultEvent = event as T
    return eventStream.ofType(event::class.java)
            .flatMap { navEvent ->
                val nestedEvents = navEvent.events
                val resultEvent = nestedEvents.filterIsInstance<ObserveResult<R>>().firstOrNull()
                val screenResult = resultEvent?.result
                if (screenResult != null) {
                    onScreenResult(screenResult)
                } else {
                    Observable.empty<T>()
                }
            }
            .mergeWith(observeResultEvent.toObservable())
}

/**
 * TODO добавить документацию
 */
fun <T : Event, R : Serializable> EventTransformerList<T>.listenForResult(
        routeClass: Class<out SupportOnActivityResultRoute<R>>,
        navigationEventFactory: EventFactory<List<NavigationEvent>, NavigationComposition>,
        onScreenResult: (ScreenResult<R>) -> Unit
): Observable<T> = listenForResultMap(
        routeClass,
        navigationEventFactory,
        { onScreenResult(it); Observable.empty() }
)