package ru.surfstudio.android.core.ui.navigation;

import java.io.Serializable;

import io.reactivex.Observable;
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute;
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithResultRoute;
import ru.surfstudio.android.core.ui.navigation.event.result.SupportOnActivityResultRoute;

/**
 * Common interface for ActivityNavigator for backward compatability
 */
public interface IActivityNavigator {

    /**
     * позволяет подписываться на событие OnActivityResult
     *
     * @param routeClass класс маршрута экрана, который должен вернуть результат
     * @param <T>        тип возвращаемых данных
     */
    <T extends Serializable> Observable<ScreenResult<T>> observeResult(
            Class<? extends SupportOnActivityResultRoute<T>> routeClass);

    @Deprecated
    <T extends Serializable> rx.Observable<ScreenResult<T>> observeResultDeprecated(
            Class<? extends SupportOnActivityResultRoute<T>> routeClass);

    /**
     * позволяет подписываться на событие OnActivityResult
     *
     * @param route маршрут экрана, который должен вернуть результат
     * @param <T>   тип возвращаемых данных
     */
    <T extends Serializable> Observable<ScreenResult<T>> observeResult(
            SupportOnActivityResultRoute route);

    /**
     * Закрываает текущую активити
     */
    void finishCurrent();

    /**
     * Закрываает текущую активити Affinity
     */
    void finishAffinity();

    /**
     * Закрываает текущую активити c результатом
     *
     * @param activeScreenRoute маршрут текущего экрана
     * @param success           показывает успешное ли завершение
     * @param <T>               тип возвращаемого значения
     */
    <T extends Serializable> void finishWithResult(ActivityWithResultRoute<T> activeScreenRoute,
                                                   boolean success);

    /**
     * Закрываает текущую активити c результатом
     *
     * @param activeScreenRoute маршрут текущего экрана
     * @param result            возвращаемый результат
     * @param <T>               тип возвращаемого значения
     */
    <T extends Serializable> void finishWithResult(SupportOnActivityResultRoute<T> activeScreenRoute,
                                                   T result);

    /**
     * Закрываает текущую активити c результатом
     *
     * @param currentScreenRoute маршрут текущего экрана
     * @param result             возвращаемый результат
     * @param success            показывает успешное ли завершение
     * @param <T>                тип возвращаемого значения
     */
    <T extends Serializable> void finishWithResult(SupportOnActivityResultRoute<T> currentScreenRoute,
                                                   T result, boolean success);

    /**
     * Launch a new activity.
     * <p>
     * Works synchronically.
     *
     * @param route navigation route
     * @return {@code true} if activity started successfully, {@code false} otherwise
     */
    boolean start(ActivityRoute route);

    /**
     * Launch a new activity for result.
     *
     * @param route navigation route
     * @return {@code true} if activity started successfully, {@code false} otherwise
     */
    boolean startForResult(SupportOnActivityResultRoute route);
}
