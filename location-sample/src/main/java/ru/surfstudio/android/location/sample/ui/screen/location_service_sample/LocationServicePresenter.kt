package ru.surfstudio.android.location.sample.ui.screen.location_service_sample

import android.location.Location
import io.reactivex.*
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.permission.PermissionManager
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.location.LocationService
import ru.surfstudio.android.location.domain.LocationPriority
import ru.surfstudio.android.location.location_errors_resolver.resolutions.LocationErrorResolution
import ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.no_location_permission.NoLocationPermissionResolution
import ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.play_services_are_not_available.PlayServicesAreNotAvailableResolution
import ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.resolveble_api_exception.ResolvableApiExceptionResolution
import ru.surfstudio.android.location.sample.ui.screen.base.BaseSamplePresenter

/**
 * Презентер экрана [LocationServiceActivityView]
 */
class LocationServicePresenter(
        basePresenterDependency: BasePresenterDependency,
        screenEventDelegateManager: ScreenEventDelegateManager,
        permissionManager: PermissionManager,
        activityProvider: ActivityProvider,
        private val locationService: LocationService
) : BaseSamplePresenter<LocationServiceActivityView>(basePresenterDependency) {

    private val resolutions = mutableListOf<LocationErrorResolution<*>>()
    private val noLocationPermissionResolution =
            NoLocationPermissionResolution(permissionManager)
    private val playServicesAreNotAvailableResolution =
            PlayServicesAreNotAvailableResolution(screenEventDelegateManager, activityProvider)
    private val resolvableApiExceptionResolution =
            ResolvableApiExceptionResolution(screenEventDelegateManager, activityProvider)

    private var locationUpdatesDisposable: Disposable = Disposables.disposed()

    fun getLocationAvailability() {
        view.showLoading()

        /**
         * Проверить возможность получения местоположения.
         */
        val checkLocationAvailabilityCompletable: Completable =
                locationService.checkLocationAvailability(LocationPriority.HIGH_ACCURACY)

        subscribeIo(
                checkLocationAvailabilityCompletable,

                /**
                 * onComplete() вызывается, если есть возможность получить местоположение.
                 */
                { hideLoadingAndShowLocationIsAvailable() },

                /**
                 * onError() вызывается, если нет возможности получить местоположение.
                 *
                 * Может прийти [CompositeException], содержащий список из возможных исключений:
                 * [NoLocationPermissionException], [PlayServicesAreNotAvailableException], [ResolvableApiException].
                 */
                { t: Throwable -> hideLoadingAndShowLocationIsNotAvailable(t) }
        )
    }

    fun getLastKnownLocation() {
        view.showLoading()

        /**
         * Запросить последнее известное местоположение.
         *
         * Принимает в качестве параметра массив решений проблем связанных с невозможностью получения местоположения.
         * Доступные решения:
         * - [NoLocationPermissionResolution];
         * - [PlayServicesAreNotAvailableResolution];
         * - [ResolvableApiExceptionResolution].
         */
        val lastKnownLocationMaybe: Maybe<Location> =
                locationService.observeLastKnownLocation(*resolutions.toTypedArray())

        subscribeIo(
                lastKnownLocationMaybe,

                /**
                 * onSuccess() вызывается в случае удачного получения местоположения.
                 */
                { location: Location -> hideLoadingAndShowLocation(location) },

                /**
                 * onComplete() вызывается в случае, если местоположение было получено, но равно null.
                 */
                { hideLoadingAndShowNoLocation() },

                /**
                 * onError() вызывается, если нет возможности получить местоположение.
                 *
                 * Могут прийти следующие исключения:
                 * - [CompositeException], содержащий список из возможных исключений:
                 * [NoLocationPermissionException], [PlayServicesAreNotAvailableException], [ResolvableApiException];
                 * - [ResolutionFailedException], если передавались экземпляры решений и попытка решения не удалась.
                 */
                { t: Throwable -> hideLoadingAndShowLocationIsNotAvailable(t) }
        )
    }

    fun subscribeToLocationUpdates() {
        /**
         * Подписаться на получение обновлений местоположения.
         *
         * Принимает в качестве параметров:
         * - интервал в миллисекундах, при котором предпочтительно получать обновления местоположения;
         * - максимальный интервал в миллисекундах, при котором возможно обрабатывать обновления местоположения;
         * - приоритет запроса (точность метостоположения/заряд батареи);
         * - массив решений проблем связанных с невозможностью получения местоположения. Доступные решения:
         *   - [NoLocationPermissionResolution]
         *   - [PlayServicesAreNotAvailableResolution]
         *   - [ResolvableApiExceptionResolution]
         */
        val locationUpdatesObservable: Observable<Location> =
                locationService.observeLocationUpdates(
                        1000,
                        1000,
                        LocationPriority.HIGH_ACCURACY,
                        *resolutions.toTypedArray()
                )

        locationUpdatesDisposable = subscribeIo(
                locationUpdatesObservable,

                /**
                 * onNext() вызывается при каждом удачном получении местоположения.
                 */
                { location: Location -> view.showLocation(location) },

                /**
                 * onComplete() никогда не вызывается.
                 */
                {},

                /**
                 * onError() вызывается, если нет возможности получить местоположение.
                 *
                 * Могут прийти следующие исключения:
                 * - [CompositeException], содержащий список из возможных исключений:
                 * [NoLocationPermissionException], [PlayServicesAreNotAvailableException], [ResolvableApiException];
                 * - [ResolutionFailedException], если передавались экземпляры решений и попытка решения не удалась.
                 */
                { t: Throwable -> view.showLocationIsNotAvailable(t) }
        )
    }

    fun unsubscribeFromLocationUpdates() {
        locationUpdatesDisposable.dispose()
    }

    fun addNoLocationPermissionResolution() {
        resolutions.add(noLocationPermissionResolution)
    }

    fun removeNoLocationPermissionResolution() {
        resolutions.remove(noLocationPermissionResolution)
    }

    fun addPlayServicesAreNotAvailableResolution() {
        resolutions.add(playServicesAreNotAvailableResolution)
    }

    fun removePlayServicesAreNotAvailableResolution() {
        resolutions.remove(playServicesAreNotAvailableResolution)
    }

    fun addResolvableApiExceptionResolution() {
        resolutions.add(resolvableApiExceptionResolution)
    }

    fun removeResolvableApiExceptionResolution() {
        resolutions.remove(resolvableApiExceptionResolution)
    }
}