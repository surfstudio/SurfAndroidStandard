package ru.surfstudio.android.location.deprecated

import android.location.Location
import androidx.annotation.RequiresPermission
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.exceptions.CompositeException
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.permission.deprecated.PermissionManager
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.location.deprecated.domain.CurrentLocationRequest
import ru.surfstudio.android.location.deprecated.domain.LastKnownLocationRequest
import ru.surfstudio.android.location.deprecated.domain.LocationPriority
import ru.surfstudio.android.location.deprecated.exceptions.NoLocationPermissionException
import ru.surfstudio.android.location.deprecated.exceptions.PlayServicesAreNotAvailableException
import ru.surfstudio.android.location.deprecated.exceptions.ResolutionFailedException
import ru.surfstudio.android.location.deprecated.location_errors_resolver.resolutions.impl.concrete.no_location_permission.LocationPermissionRequest
import ru.surfstudio.android.location.deprecated.location_errors_resolver.resolutions.impl.concrete.no_location_permission.NoLocationPermissionResolution
import ru.surfstudio.android.location.deprecated.location_errors_resolver.resolutions.impl.concrete.play_services_are_not_available.PlayServicesAreNotAvailableResolution
import ru.surfstudio.android.location.deprecated.location_errors_resolver.resolutions.impl.concrete.resolveble_api_exception.ResolvableApiExceptionResolution
import java.util.concurrent.TimeUnit

/**
 * Интерактор, содержащий методы для наиболее частых случаев использования. Для более гибкой настройки следует
 * использовать [LocationService].
 */
class DefaultLocationInteractor(
        private val permissionManager: PermissionManager,
        private val screenEventDelegateManager: ScreenEventDelegateManager,
        private val activityProvider: ActivityProvider,
        private val locationService: LocationService
) {

    private var lastCurrentLocation: Location? = null
    private var lastCurrentLocationPriority: LocationPriority? = null

    /**
     * Проверить возможность получения местоположения.
     *
     * @return [Completable]:
     * - onComplete() вызывается, если есть возможность получить местоположение;
     * - onError() вызывается, если нет возможности получить местоположение. Приходит [CompositeException], содержащий
     * список из возможных исключений: [NoLocationPermissionException], [PlayServicesAreNotAvailableException],
     * [ResolvableApiException].
     */
    fun checkLocationAvailability(locationPriority: LocationPriority): Completable =
            locationService.checkLocationAvailability(locationPriority)

    /**
     * Решить проблемы связанные с невозможностью получения местоположения.
     *
     * @param throwables [List], содержащий исключения связанные с невозможностью получения местоположения.
     * @param locationPermissionRequest Запрос разрешения на доступ к местоположению, используемый в
     * [NoLocationPermissionResolution].
     *
     * @return [Single]:
     * - onSuccess() вызывается при удачном решении проблем. Содержит [List] из нерешенных исключений, для которых не
     * передавались решения;
     * - onError() вызывается в случае, если попытка решения проблем не удалась. Приходит [ResolutionFailedException].
     */
    @RequiresPermission(
            allOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"]
    )
    fun resolveLocationAvailability(
            throwables: List<Throwable>,
            locationPermissionRequest: LocationPermissionRequest = LocationPermissionRequest()
    ): Single<List<Throwable>> =
            locationService.resolveLocationAvailability(
                    throwables,
                    NoLocationPermissionResolution(permissionManager, locationPermissionRequest),
                    PlayServicesAreNotAvailableResolution(screenEventDelegateManager, activityProvider),
                    ResolvableApiExceptionResolution(screenEventDelegateManager, activityProvider)
            )

    /**
     * Запросить последнее известное местоположение.
     *
     * @param lastKnownLocationRequest Запрос последнего известного местоположения.
     *
     * @return [Maybe]:
     * - onSuccess() вызывается в случае удачного получения местоположения;
     * - onComplete() вызывается в случае, если местоположение было получено, но равно null;
     * - onError() вызывается, если нет возможности получить местоположение. Могут прийти:
     * - [CompositeException], содержащий список из возможных исключений: [NoLocationPermissionException],
     * [PlayServicesAreNotAvailableException], [ResolvableApiException];
     * - [ResolutionFailedException].
     */
    @RequiresPermission(
            allOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"]
    )
    fun observeLastKnownLocationWithErrorsResolution(
            lastKnownLocationRequest: LastKnownLocationRequest
    ): Maybe<Location> =
            locationService
                    .checkLocationAvailability(lastKnownLocationRequest.priority)
                    .onErrorResumeNext { t: Throwable ->
                        resolveAllLocationErrorsIfNeededAndPossible(
                                lastKnownLocationRequest.resolveLocationErrors,
                                t,
                                lastKnownLocationRequest.locationPermissionRequest
                        )
                    }
                    .andThen(locationService.observeLastKnownLocation())
                    .doOnSuccess {
                        lastCurrentLocation = it
                        lastCurrentLocationPriority = lastKnownLocationRequest.priority
                    }

    /**
     * Запросить текущее местоположение.
     *
     * @param currentLocationRequest Запрос текущего местоположения.
     *
     * @return [Single]:
     * - onSuccess() вызывается в случае удачного получения местоположения.
     * - onError() вызывается, если нет возможности получить местоположение. Могут прийти:
     *   - [CompositeException], содержащий список из возможных исключений: [NoLocationPermissionException],
     *   [PlayServicesAreNotAvailableException], [ResolvableApiException];
     *   - [ResolutionFailedException];
     *   - [TimeoutException].
     */
    @RequiresPermission(
            allOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"]
    )
    fun observeCurrentLocationWithErrorsResolution(currentLocationRequest: CurrentLocationRequest): Single<Location> {
        val relevantLocation = getRelevantLocationOrNull(
                currentLocationRequest.relevanceTimeoutMillis,
                currentLocationRequest.priority
        )

        if (relevantLocation != null) {
            return Single.just(relevantLocation)
        }

        return locationService
                .checkLocationAvailability(currentLocationRequest.priority)
                .onErrorResumeNext { t: Throwable ->
                    resolveAllLocationErrorsIfNeededAndPossible(
                            currentLocationRequest.resolveLocationErrors,
                            t,
                            currentLocationRequest.locationPermissionRequest
                    )
                }
                .andThen(observeLocationUpdates(currentLocationRequest))
                .firstOrError()
                .doOnSuccess {
                    lastCurrentLocation = it
                    lastCurrentLocationPriority = currentLocationRequest.priority
                }
    }

    private fun resolveAllLocationErrorsIfNeededAndPossible(
            resolveLocationErrors: Boolean,
            t: Throwable,
            locationPermissionRequest: LocationPermissionRequest
    ): Completable =
            if (resolveLocationErrors && (t is CompositeException)) {
                resolveAllLocationErrors(t.exceptions, locationPermissionRequest)
            } else {
                Completable.error(t)
            }

    private fun resolveAllLocationErrors(
            throwables: List<Throwable>,
            locationPermissionRequest: LocationPermissionRequest
    ): Completable =
            resolveLocationAvailability(throwables, locationPermissionRequest)
                    .flatMap {
                        if (it.isNotEmpty()) {
                            Single.error(CompositeException(it))
                        } else {
                            Single.just(it)
                        }
                    }
                    .ignoreElement()

    private fun getRelevantLocationOrNull(relevanceTimeoutMillis: Long, locationPriority: LocationPriority): Location? {
        val nonNullLastCurrentLocation = lastCurrentLocation ?: return null
        val nonNullLastCurrentLocationPriority = lastCurrentLocationPriority ?: return null

        val currentTimeMillis = System.currentTimeMillis()
        val deltaTimeMillis = currentTimeMillis - nonNullLastCurrentLocation.time

        return when {
            deltaTimeMillis > relevanceTimeoutMillis -> null
            LocationPriority.compare(locationPriority, nonNullLastCurrentLocationPriority) < 0 -> null
            else -> nonNullLastCurrentLocation
        }
    }

    @RequiresPermission(
            anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"]
    )
    private fun observeLocationUpdates(currentLocationRequest: CurrentLocationRequest): Observable<Location> {
        var locationUpdatesObservable = locationService.observeLocationUpdates(0, 0, currentLocationRequest.priority)

        if (currentLocationRequest.expirationTimeoutMillis > 0) {
            locationUpdatesObservable =
                    locationUpdatesObservable
                            .timeout(currentLocationRequest.expirationTimeoutMillis, TimeUnit.MILLISECONDS)
        }

        return locationUpdatesObservable
    }
}