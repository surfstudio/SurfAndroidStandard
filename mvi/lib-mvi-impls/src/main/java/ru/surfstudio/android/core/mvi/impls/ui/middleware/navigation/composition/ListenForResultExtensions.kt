package ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.factory.EventFactory
import ru.surfstudio.android.core.mvi.impls.ui.middleware.dsl.EventTransformerList
import ru.surfstudio.android.core.mvi.event.navigation.NavigationEvent
import ru.surfstudio.android.core.ui.event.result.SupportOnActivityResultRoute
import ru.surfstudio.android.core.ui.navigation.ScreenResult
import ru.surfstudio.android.rx.extension.toObservable
import java.io.Serializable

/**
 * Listen for screen result events and execute [onScreenResult] when the result will appear.
 *
 * @param routeClass                class of route, which will be observed
 * @param navigationEventFactory    factory that creates navigation event
 * @param onScreenResult            callback, which will be executed when screen result is appeared
 */
fun <T : Event, R : Serializable> EventTransformerList<T>.listenForResultMap(
        routeClass: Class<out SupportOnActivityResultRoute<R>>,
        navigationEventFactory: EventFactory<List<NavigationEvent>, T>,
        onScreenResult: (ScreenResult<R>) -> Observable<T>
): Observable<T> {
    val event = navigationEventFactory.invoke(listOf(ObserveResult(routeClass))) //create listen for result event
    return eventStream.ofType(event::class.java)
            .flatMap { uncastedEvent ->
                val navEvent = uncastedEvent as? NavigationComposition
                val nestedEvents = navEvent?.events
                val resultEvent = nestedEvents?.filterIsInstance<ObserveResult<R>>()?.firstOrNull()
                val screenResult = resultEvent?.result
                if (screenResult != null) {
                    onScreenResult(screenResult)
                } else {
                    Observable.empty<T>()
                }
            }
            .mergeWith(event.toObservable())
}

/**
 * Listen for screen result events and execute [onScreenResult] when the result will appear.
 *
 * @param routeClass                class of route, which will be observed
 * @param navigationEventFactory    factory that creates navigation event
 * @param onScreenResult            callback, which will be executed when screen result is appeared
 */
fun <T : Event, R : Serializable> EventTransformerList<T>.listenForResult(
        routeClass: Class<out SupportOnActivityResultRoute<R>>,
        navigationEventFactory: EventFactory<List<NavigationEvent>, T>,
        onScreenResult: (ScreenResult<R>) -> Unit
): Observable<T> = listenForResultMap(
        routeClass,
        navigationEventFactory,
        { onScreenResult(it); Observable.empty<T>() }
)