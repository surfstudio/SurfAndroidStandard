package ru.surfstudio.android.location

import android.content.Intent
import io.reactivex.Observable
import ru.surfstudio.android.core.app.dagger.scope.PerScreen
import ru.surfstudio.android.core.ui.base.delegate.activity.result.ActivityResultDelegate
import ru.surfstudio.android.core.ui.base.navigation.activity.route.ActivityWithResultRoute
import ru.surfstudio.android.location.dialog.LocationDeniedDialogData
import javax.inject.Inject

/**
 * Сервис, через который осуществляется работа с геолокационными данными.
 *
 * Предоставляет несколько вариантов получения геолокации: безопасный доступ, опасный доступ,
 * тихая проверка пермишна и другие.
 */
@PerScreen
class LocationSafeInteractor @Inject constructor(val locationServiceChecker: LocationServiceChecker,
                                                 private val locationService: LocationService)
    : ActivityResultDelegate {

    /**
     * Безопасное определение текущей геолокации пользователя.
     * Перед попыткой получить данные о текущей геолокации пользователя происходит проверка:
     *
     * наличия на устройстве установленных Google Play Services;
     * разрешения на доступ к геолокационному сервису у приложения;
     * включённости GPS на устройстве.
     *
     * @param shouldReset - необходимо ли обновить данные сервиса
     *
     * @see LocationServiceChecker.checkAndResolveLocationServiceAvailability(LocationDeniedDialogData)
     */
    fun getLocation(data: LocationDeniedDialogData, shouldReset: Boolean = false, locationOnFail: LocationData = UNKNOWN_LOCATION): Observable<LocationData> =
            locationServiceChecker.checkAndResolveLocationServiceAvailability(data)
                    .flatMap { isLocationServiceAvailable: Boolean ->
                        when (isLocationServiceAvailable) {
                            true -> locationService.getLocation(shouldReset = shouldReset)
                            false -> Observable.just(locationOnFail)
                        }
                    }


    /**
     * @see getLocation(LocationDeniedDialogData, Boolean)
     * @see LocationServiceChecker.checkAndResolveLocationServiceAvailability(ActivityWithResult)
     */
    fun getLocation(route: ActivityWithResultRoute<Boolean>, shouldReset: Boolean = false, locationOnFail: LocationData = UNKNOWN_LOCATION): Observable<LocationData> =
            locationServiceChecker.checkAndResolveLocationServiceAvailability(route)
                    .flatMap { isLocationServiceAvailable: Boolean ->
                        when (isLocationServiceAvailable) {
                            true -> locationService.getLocation(shouldReset = shouldReset)
                            false -> Observable.just(locationOnFail)
                        }
                    }


    /**
     * Опасное определение текущей геолокации пользователя.
     *
     * Доступность геолокации не проверяется, возможные проблемы с доступом к геолокации никак не
     * решаются. В случае возникновения проблем с получением пользовательской геолокации возвращается
     * null.
     */
    fun getLocationUnsafe(): Observable<LocationData?> = locationService.getLocation()

    /**
     * Тихая проверка пермишнов на доступ к геолокации. Если пермишны не выданы -
     * они не перезапрашиваются.
     */
    fun areLocationServicePermissionsGranted(): Boolean =
            locationServiceChecker.areLocationServicePermissionsGranted()

    /**
     * Подписка на включение геолокации через настройки
     */
    fun observeLocationEnabled(): Observable<Boolean> =
            locationServiceChecker.observeLocationEnabled()

    /**
     * Проброс события выдачи пермишна на доступ к геолокации
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        return locationServiceChecker.onActivityResult(requestCode, resultCode, data)
    }

    fun isAvailable(): Boolean {
        return locationServiceChecker.isAvailable(locationService.locationProvider) && areLocationServicePermissionsGranted()
    }

}