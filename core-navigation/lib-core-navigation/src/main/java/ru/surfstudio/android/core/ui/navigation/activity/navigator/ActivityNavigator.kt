package ru.surfstudio.android.core.ui.navigation.activity.navigator

import io.reactivex.Observable
import ru.surfstudio.android.core.ui.navigation.Navigator
import ru.surfstudio.android.core.ui.navigation.ScreenResult
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithResultRoute
import ru.surfstudio.android.core.ui.navigation.activity.route.NewIntentRoute
import ru.surfstudio.android.core.ui.navigation.event.result.CrossFeatureSupportOnActivityResultRoute
import ru.surfstudio.android.core.ui.navigation.event.result.SupportOnActivityResultRoute
import ru.surfstudio.android.core.ui.navigation.feature.installer.SplitFeatureInstallState
import ru.surfstudio.android.core.ui.navigation.feature.route.feature.ActivityCrossFeatureRoute
import java.io.Serializable


/**
 * позволяет осуществлять навигацияю между активити
 * <p>
 * !!!В случае конфликтов возвращения результата между несколькими инстансами навигаторами
 * можно рассмотреть добавление к RequestCode хеша от имени экрана контейнера
 * Конфликт может возникнуть при открытии одинаковых экранов из, например, кастомной вью с
 * презентером и родительской активити вью
 */
interface ActivityNavigator : Navigator {

    /**
     * позволяет подписываться на событие OnActivityResult
     *
     * @param routeClass класс маршрута экрана, который должен вернуть результат
     * @param <T>        тип возвращаемых данных
     */
    fun <T : Serializable> observeResult(routeClass: Class<out SupportOnActivityResultRoute<T>>): Observable<ScreenResult<T?>>

    /**
     * позволяет подписываться на событие OnActivityResult
     *
     * @param route маршрут экрана, который должен вернуть результат
     * @param <T>   тип возвращаемых данных
     */
    fun <T : Serializable> observeResult(route: SupportOnActivityResultRoute<T>): Observable<ScreenResult<T?>>

    /**
     * Закрываает текущую активити
     */
    fun finishCurrent()

    /**
     * Закрываает текущую активити Affinity
     */
    fun finishAffinity()

    /**
     * Закрываает текущую активити c результатом
     *
     * @param route маршрут текущего экрана
     * @param success           показывает успешное ли завершение
     * @param <T>               тип возвращаемого значения
     */
    fun <T : Serializable> finishWithResult(route: ActivityWithResultRoute<T>, success: Boolean)

    /**
     * Закрываает текущую активити c результатом
     *
     * @param activeScreenRoute маршрут текущего экрана
     * @param result            возвращаемый результат
     * @param <T>               тип возвращаемого значения
     */
    fun <T : Serializable> finishWithResult(activeScreenRoute: SupportOnActivityResultRoute<T>, result: T?)

    /**
     * Закрываает текущую активити c результатом
     *
     * @param currentScreenRoute маршрут текущего экрана
     * @param result             возвращаемый результат
     * @param success            показывает успешное ли завершение
     * @param <T>                тип возвращаемого значения
     */
    fun <T : Serializable> finishWithResult(currentScreenRoute: SupportOnActivityResultRoute<T>, result: T?, success: Boolean)

    /**
     * Launch a new activity.
     * <p>
     * Works synchronously.
     *
     * @param route navigation route
     * @return true if activity started successfully, false otherwise
     */
    fun start(route: ActivityRoute): Boolean

    /**
     * Launch a new activity from another Feature Module.
     * <p>
     * Performs asynchronously due to type of the target Feature Module.
     * This method returns stream of install state change events. You can make a subscription in
     * your Presenter and handle errors or any other type of events during Dynamic Feature
     * installation.
     *
     * @param route navigation route
     * @return stream of install state change events
     */
    fun start(route: ActivityCrossFeatureRoute): Observable<SplitFeatureInstallState>

    /**
     * Launch a new Activity for result from another Feature Module.
     * <p>
     * Performs asynchronously due to type of the target Feature Module.
     * This method returns stream of install state change events. You can make a subscription in
     * your Presenter and handle errors or any other type of events during Dynamic Feature
     * installation.
     *
     * @param route navigation route
     * @return stream of install state change events
     */
    fun startForResult(route: CrossFeatureSupportOnActivityResultRoute<*>): Observable<SplitFeatureInstallState>

    /**
     * Launch a new activity for result.
     *
     * @param route navigation route
     * @return true if activity started successfully, false otherwise
     */
    fun startForResult(route: SupportOnActivityResultRoute<*>): Boolean

    /**
     * позволяет подписываться на событие OnNewIntent
     *
     * @param newIntentRouteClass класс, отвечающий за парсинг intent
     */
    fun <T : NewIntentRoute> observeNewIntent(newIntentRouteClass: Class<T>): Observable<T>

    /**
     * позволяет подписываться на событие OnNewIntent
     *
     * @param newIntentRoute отвечает за парсинг intent
     */
    fun <T : NewIntentRoute> observeNewIntent(newIntentRoute: T): Observable<T>
}