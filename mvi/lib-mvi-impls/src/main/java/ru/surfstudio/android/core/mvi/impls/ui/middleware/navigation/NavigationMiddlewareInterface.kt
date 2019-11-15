package ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.impls.ui.middleware.dsl.EventTransformerList
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.close.*
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.open.OpenScreenEvent
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.mvi.util.filterIsInstance
import ru.surfstudio.android.core.ui.event.result.SupportOnActivityResultRoute
import ru.surfstudio.android.core.ui.navigation.ScreenResult
import java.io.Serializable

/**
 * Интерфейс для middleware с поддержкой навигации.
 *
 * Обрабатывает события [OpenScreenEvent] и [CloseScreenEvent], определяя нужные навигаторы по классам
 *
 * Является экспериментальной фичей и не рекомендуется для использования на стабильных проектах.
 */
interface NavigationMiddlewareInterface<T : Event> : RxMiddleware<T> {

    var screenNavigator: ScreenNavigator

    /**
     * Opens the screen when [OpenScreenEvent] appears on eventStream.
     */
    fun openScreenByEvent(event: OpenScreenEvent) {
        screenNavigator.open(event.route)
    }

    /**
     * Closes the screen when [CloseScreenEvent] appears on eventStream.
     */
    fun closeScreenByEvent(event: CloseScreenEvent) {
        when (event) {
            is CloseActivityEvent ->
                screenNavigator.close()

            is CloseTaskEvent ->
                screenNavigator.closeTask()

            is CloseWithResultEvent<*> -> {
                val resultRoute = event.route as SupportOnActivityResultRoute<Serializable>
                screenNavigator.closeWithResult(resultRoute, event.result, event.isSuccess)
            }

            is CloseFragmentEvent -> {
                screenNavigator.close(event.route)
            }

            is CloseDialogEvent -> {
                screenNavigator.close(event.route)
            }
        }
    }

    /**
     * Automatically opens/closes screen when [CloseScreenEvent] or [OpenScreenEvent]
     * appears in eventStream.
     */
    fun Observable<T>.mapNavigationAuto() =
            flatMap { event ->
                when (event) {
                    is OpenScreenEvent -> openScreenByEvent(event)
                    is CloseScreenEvent -> closeScreenByEvent(event)
                }
                Observable.empty<T>()
            }

    /**
     * Listens for screen result of specific route.
     * Used whe the screen result should be processed on eventStream chain.
     *
     * @param routeClass screen route class
     * @param screenResultMapper mapper, which triggers on [CloseWithResultEvent] and
     * transforms result in Observable<T> to pass it to eventStream.
     */
    fun <R : Serializable> Observable<T>.listenForResultMap(
            routeClass: Class<out SupportOnActivityResultRoute<R>>,
            screenResultMapper: (ScreenResult<R>) -> Observable<T>
    ): Observable<out T> {

        val observeRoute = screenNavigator.observeResult(routeClass)
                .flatMap(screenResultMapper)

        val openScreen = filterIsInstance<OpenScreenEvent>()
                .filter { routeClass.isInstance(it.route) }
                .flatMap {
                    screenNavigator.open(it.route)
                    Observable.empty<T>()
                }

        return merge(observeRoute, openScreen)
    }

    /**
     * Listens for screen result of specific route.
     * Used whe the screen result should be processed on eventStream chain.
     *
     * @param routeClass screen route class
     * @param screenResultMapper mapper, which triggers on [CloseWithResultEvent] and
     * transforms result in Observable<T> to pass it to eventStream.
     */
    fun <D : Serializable> EventTransformerList<T>.listenForResultMap(
            routeClass: Class<out SupportOnActivityResultRoute<D>>,
            screenResultMapper: (ScreenResult<D>) -> Observable<T>
    ): Observable<out T> = eventStream.listenForResultMap(routeClass, screenResultMapper)

    /**
     * Listens for screen result of specific route.
     * Used whe the screen result should be simply reacted on.
     *
     * @param routeClass screen route class
     * @param screenResultCallback callback, which is called on [CloseWithResultEvent]
     */
    fun <D : Serializable> Observable<T>.listenForResult(
            routeClass: Class<out SupportOnActivityResultRoute<D>>,
            screenResultCallback: (ScreenResult<D>) -> Unit
    ): Observable<out T> =
            listenForResultMap(routeClass) {
                screenResultCallback(it)
                Observable.empty()
            }

    /**
     * Listens for screen result of specific route.
     * Used whe the screen result should be simply reacted on.
     *
     * @param routeClass screen route class
     * @param screenResultCallback callback, which is called on [CloseWithResultEvent]
     */
    fun <D : Serializable> EventTransformerList<T>.listenForResult(
            routeClass: Class<out SupportOnActivityResultRoute<D>>,
            screenResultCallback: (ScreenResult<D>) -> Unit
    ): Observable<out T> = eventStream.listenForResult(routeClass, screenResultCallback)

}