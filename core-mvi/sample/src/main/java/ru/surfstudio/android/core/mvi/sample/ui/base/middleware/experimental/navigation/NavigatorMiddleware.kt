package ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.navigation

import android.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE
import android.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.ExperimentalFeature
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.navigation.close.*
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.mvi.ui.middleware.dsl.EventTransformerList
import ru.surfstudio.android.core.mvi.util.filterIsInstance
import ru.surfstudio.android.core.ui.event.result.SupportOnActivityResultRoute
import ru.surfstudio.android.core.ui.navigation.ScreenResult
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator
import ru.surfstudio.android.mvp.dialog.navigation.route.DialogRoute
import java.io.Serializable

/**
 * Интерфейс для middleware с поддержкой навигации.
 *
 * Обрабатывает события [OpenScreenEvent] и [CloseScreenEvent], определяя нужные навигаторы по классам
 *
 * Является экспериментальной фичей и не рекомендуется для использования на стабильных проектах.
 */
@ExperimentalFeature
interface NavigatorMiddleware<T : Event> : RxMiddleware<T> {

    val activityNavigator: ActivityNavigator
    val fragmentNavigator: FragmentNavigator
    val dialogNavigator: DialogNavigator

    val observableRoutes: MutableList<Class<out SupportOnActivityResultRoute<*>>>

    /**
     * Функция открытия экрана по событию, поступающему на Middleware.
     * При необходимости, можн переопределить ее для задания необходимого поведения
     * (например, для задания transition фрагментам или диалогам.)
     */
    fun openScreenByEvent(event: OpenScreenEvent) {
        when (val route = event.route) {
            is ActivityRoute -> {
                if (observableRoutes.contains(route::class.java)) {
                    return //если мы обрабатываем открытие OnActivityResult в другом методе - значит не трогаем его здесь
                }
                activityNavigator.start(route)
            }

            is DialogRoute -> {
                dialogNavigator.show(route)
            }

            is FragmentRoute -> {
                fragmentNavigator.add(route, true, TRANSIT_FRAGMENT_OPEN)
            }
        }
    }

    /**
     * Функция закрытия экрана по событию, поступающему на Middleware.
     * При необходимости, можн переопределить ее для задания необходимого поведения
     * (например, для задания transition фрагментам или диалогам.)
     */
    fun closeScreenByEvent(event: CloseScreenEvent) {
        when (event) {
            is CloseActivityEvent ->
                activityNavigator.finishCurrent()
            is CloseAffinityEvent ->
                activityNavigator.finishAffinity()
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

    /**
     * Автоматическое открытие экрана при поступлении события [OpenScreenEvent]
     */
    fun EventTransformerList<T>.openScreenAuto() = eventStream.openScreenAuto()

    /**
     * Автоматическое открытие экрана при поступлении события [OpenScreenEvent]
     */
    fun Observable<T>.openScreenAuto() =
            filterIsInstance<OpenScreenEvent>().flatMap {
                openScreenByEvent(it)
                Observable.empty<T>()
            }

    /**
     * Автоматическое закрытие экрана при поступлении события [CloseScreenEvent]
     */
    fun Observable<T>.closeScreenDefault() = filterIsInstance<CloseScreenEvent>()
            .flatMap { closeScreenByEvent(it).skip<T>() }

    /**
     * Автоматическое закрытие экрана при поступлении события [CloseScreenEvent]
     */
    fun Observable<T>.mapNavigationAuto() =
            merge(openScreenAuto(), closeScreenDefault())

    /**
     * Автоматическое закрытие и открытие экранов при поступлении
     * событий [CloseScreenEvent] и [OpenScreenEvent]
     */
    fun EventTransformerList<T>.mapNavigationAuto() = eventStream.mapNavigationAuto()

    /**
     * Установка слушателя результата работы экрана, вызываемого
     * при поступлении события [CloseWithResultEvent].
     * Используется, когда результат экрана нужно преобразовать в event.
     *
     * @param routeClass класс роута экрана, который необходимо прослушать
     * @param screenResultMapper маппер, срабатывающий при получении результата с экрана,
     * и трансформирующий результат в Observable<Event> для дальнейшего проброса в eventStream
     */
    fun <D : Serializable> Observable<T>.listenForResultMap(
            routeClass: Class<out SupportOnActivityResultRoute<D>>,
            screenResultMapper: (ScreenResult<D>) -> Observable<T>
    ): Observable<out T> {

        observableRoutes.add(routeClass)

        val observeRoute = activityNavigator.observeResult(routeClass)
                .flatMap {
                    screenResultMapper(it)
                }


        val openScreen = filterIsInstance<OpenScreenEvent>()
                .filter { routeClass.isInstance(it.route) }
                .flatMap {
                    val route = it.route as SupportOnActivityResultRoute<D>
                    activityNavigator.startForResult(route).skip<T>()
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
     * @param screenResultCallback коллбек, срабатываемый при получении результата с экрана
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
     * @param screenResultCallback коллбек, срабатываемый при получении результата с экрана
     */
    fun <D : Serializable> EventTransformerList<T>.listenForResult(
            routeClass: Class<out SupportOnActivityResultRoute<D>>,
            screenResultCallback: (ScreenResult<D>) -> Unit
    ): Observable<out T> = eventStream.listenForResult(routeClass, screenResultCallback)

}