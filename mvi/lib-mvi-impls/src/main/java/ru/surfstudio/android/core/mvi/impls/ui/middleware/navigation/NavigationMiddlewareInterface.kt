package ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.impls.ui.middleware.dsl.EventTransformerList
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.close.*
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
     * Функция открытия экрана по событию, поступающему на Middleware.
     * При необходимости, можно переопределить ее для задания необходимого поведения
     * (например, для задания transition фрагментам или диалогам.)
     */
    fun openScreenByEvent(event: OpenScreenEvent) {
        screenNavigator.open(event.route)
    }

    /**
     * Функция закрытия экрана по событию, поступающему на Middleware.
     * При необходимости, можно переопределить ее для задания необходимого поведения
     * (например, для задания transition фрагментам или диалогам.)
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
     * Автоматическое закрытие экрана при поступлении события [CloseScreenEvent]
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
     * Установка слушателя результата работы экрана, вызываемого
     * при поступлении события [CloseWithResultEvent].
     * Используется, когда результат экрана нужно преобразовать в event.
     *
     * @param routeClass класс роута экрана, который необходимо прослушать
     * @param screenResultMapper маппер, срабатывающий при получении результата с экрана,
     * и трансформирующий результат в Observable<Event> для дальнейшего проброса в eventStream
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
     * Установка слушателя результата работы экрана, вызываемого
     * при поступлении события [CloseWithResultEvent].
     * Используется, когда результат экрана нужно преобразовать в event.
     *
     * @param routeClass класс роута экрана, который необходимо прослушать
     * @param screenResultMapper маппер, срабатывающий при получении результата с экрана,
     * и трансформирующий результат в Observable<Event> для дальнейшего проброса в eventStream
     */
    fun <D : Serializable> EventTransformerList<T>.listenForResultMap(
            routeClass: Class<out SupportOnActivityResultRoute<D>>,
            screenResultMapper: (ScreenResult<D>) -> Observable<T>
    ): Observable<out T> = eventStream.listenForResultMap(routeClass, screenResultMapper)

    /**
     * Установка слушателя результата работы экрана, вызываемого
     * при поступлении события [CloseWithResultEvent].
     * Используется, когда на результат экрана нужно просто прореагировать.
     *
     * @param routeClass класс роута экрана, который необходимо прослушать
     * @param screenResultCallback коллбек, вызываемый при получении результата с экрана
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
     * Установка слушателя результата работы экрана, вызываемого
     * при поступлении события [CloseWithResultEvent].
     * Используется, когда на результат экрана нужно просто прореагировать.
     *
     * @param routeClass класс роута экрана, который необходимо прослушать
     * @param screenResultCallback коллбек, вызываемый при получении результата с экрана
     */
    fun <D : Serializable> EventTransformerList<T>.listenForResult(
            routeClass: Class<out SupportOnActivityResultRoute<D>>,
            screenResultCallback: (ScreenResult<D>) -> Unit
    ): Observable<out T> = eventStream.listenForResult(routeClass, screenResultCallback)

}