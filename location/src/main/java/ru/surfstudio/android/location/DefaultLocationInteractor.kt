package ru.surfstudio.android.location

import android.location.Location
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.exceptions.CompositeException
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.permission.PermissionManager
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.location.domain.LocationPriority
import ru.surfstudio.android.location.exceptions.NoLocationPermissionException
import ru.surfstudio.android.location.exceptions.PlayServicesAreNotAvailableException
import ru.surfstudio.android.location.exceptions.ResolutionFailedException
import ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.no_location_permission.NoLocationPermissionResolution
import ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.play_services_are_not_available.PlayServicesAreNotAvailableResolution
import ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.resolveble_api_exception.ResolvableApiExceptionResolution

private const val DEFAULT_RELEVANCE_TIMEOUT_MILLIS = 5_000L
private val DEFAULT_LOCATION_PRIORITY = LocationPriority.BALANCED_POWER_ACCURACY

/**
 * Интерактор, содержащий методы для наиболее частых случаев использования. Для более гибкой настройки следует
 * использовать [LocationService].
 */
class DefaultLocationInteractor(
        permissionManager: PermissionManager,
        screenEventDelegateManager: ScreenEventDelegateManager,
        activityProvider: ActivityProvider,
        private val locationService: LocationService
) {

    private var lastCurrentLocation: Location? = null

    private val resolutions = arrayOf(
            NoLocationPermissionResolution(permissionManager),
            PlayServicesAreNotAvailableResolution(screenEventDelegateManager, activityProvider),
            ResolvableApiExceptionResolution(screenEventDelegateManager, activityProvider)
    )

    /**
     * Проверить возможность получения местоположения.
     *
     * @return [Completable]:
     * - onComplete() вызывается, если есть возможность получить местоположение;
     * - onError() вызывается, если нет возможности получить местоположение. Приходит [CompositeException], содержащий
     * список из возможных исключений: [NoLocationPermissionException], [PlayServicesAreNotAvailableException],
     * [ResolvableApiException].
     */
    fun observeLocationAvailability(): Completable =
            locationService.observeLocationAvailability(DEFAULT_LOCATION_PRIORITY)

    /**
     * Решить проблемы связанные с невозможностью получения местоположения.
     *
     * @param throwables [List], содержащий исключения связанные с невозможностью получения местоположения.
     *
     * @return [Single]:
     * - onSuccess() вызывается при удачном решении проблем. Содержит [List] из нерешенных исключений, для которых не
     * передавались решения;
     * - onError() вызывается в случае, если попытка решения проблем не удалась. Приходит [ResolutionFailedException].
     */
    fun observeLocationAvailabilityResolving(throwables: List<Throwable>): Single<List<Throwable>> =
            locationService.observeLocationAvailabilityResolving(throwables, *resolutions)

    /**
     * Запросить последнее известное местоположение.
     *
     * @return [Maybe]:
     * - onSuccess() вызывается в случае удачного получения местоположения;
     * - onComplete() вызывается в случае, если местоположение было получено, но равно null;
     * - onError() вызывается, если нет возможности получить местоположение. Приходит [CompositeException], содержащий
     * список из возможных исключений: [NoLocationPermissionException], [PlayServicesAreNotAvailableException],
     * [ResolvableApiException].
     */
    fun observeLastKnownLocation(): Maybe<Location> =
            locationService
                    .observeLastKnownLocation()
                    .doOnSuccess { lastCurrentLocation = it }

    /**
     * Запросить текущее местоположение.
     *
     * @param relevanceTimeoutMillis таймаут, при котором последнее полученное местоположение актуально. (по умолчанию 5
     * секунд). В случае, если местоположение ещё актуально, запрос не инициируется, а сразу
     * возвращается закешированное значение.
     *
     * @return [Single]:
     * - onSuccess() вызывается в случае удачного получения местоположения.
     * - onError() вызывается, если нет возможности получить местоположение. Приходит [CompositeException], содержащий
     * список из возможных исключений: [NoLocationPermissionException], [PlayServicesAreNotAvailableException],
     * [ResolvableApiException].
     */
    fun observeCurrentLocation(relevanceTimeoutMillis: Long = DEFAULT_RELEVANCE_TIMEOUT_MILLIS): Single<Location> {
        val relevantLocation = getRelevantLocationOrNull(relevanceTimeoutMillis)
        return if (relevantLocation != null) {
            Single.just(relevantLocation)
        } else {
            locationService
                    .observeLocationUpdates(0, 0, DEFAULT_LOCATION_PRIORITY)
                    .firstOrError()
                    .doOnSuccess { lastCurrentLocation = it }
        }
    }

    private fun getRelevantLocationOrNull(relevanceTimeoutMillis: Long): Location? {
        val nonNullLastCurrentLocation = lastCurrentLocation ?: return null
        val currentTimeMillis = System.currentTimeMillis()
        val deltaTimeMillis = currentTimeMillis - nonNullLastCurrentLocation.time

        return if (deltaTimeMillis > relevanceTimeoutMillis) {
            null
        } else {
            nonNullLastCurrentLocation
        }
    }
}