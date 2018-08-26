package ru.surfstudio.android.location

import android.content.Context
import android.location.Location
import io.reactivex.Maybe
import io.reactivex.Single
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.permission.PermissionManager
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.location.domain.LocationPriority
import ru.surfstudio.android.location.location_errors_resolver.resolutions.LocationErrorResolution
import ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.no_location_permission.NoLocationPermissionResolution
import ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.play_services_are_not_available.PlayServicesAreNotAvailableResolution
import ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.resolveble_api_exception.ResolvableApiExceptionResolution

private const val DEFAULT_RELEVANCE_TIME_MILLIS = 5_000L

/**
 * Интерактор, содержащий методы для наиболее частых случаев использования. Для более гибкой настройки следует
 * использовать [LocationService].
 */
class DefaultLocationInteractor(
        context: Context,
        permissionManager: PermissionManager,
        screenEventDelegateManager: ScreenEventDelegateManager,
        activityProvider: ActivityProvider
) {

    private val locationService = LocationService(context)

    private var lastCurrentLocation: Location? = null
    private var lastCurrentLocationRequestTimeMillis: Long? = null

    private val resolutions = arrayOf(
            NoLocationPermissionResolution(permissionManager),
            PlayServicesAreNotAvailableResolution(screenEventDelegateManager, activityProvider),
            ResolvableApiExceptionResolution(screenEventDelegateManager, activityProvider)
    )

    /**
     * Запросить последнее известное местоположение.
     */
    fun observeLastKnownLocation(): Maybe<Location> = observeLastKnownLocationWithErrorResolution(emptyArray())

    /**
     * Запросить последнее известное местоположение с решением возникающих проблем.
     */
    fun observeLastKnownLocationWithErrorResolution(): Maybe<Location> =
            observeLastKnownLocationWithErrorResolution(resolutions)

    /**
     * Запросить текущее местоположение.
     *
     * @param relevanceTimeoutMillis таймаут, при котором последнее полученное местоположение актуально.
     */
    fun observeCurrentLocation(relevanceTimeoutMillis: Long = DEFAULT_RELEVANCE_TIME_MILLIS): Single<Location> =
            observeCurrentLocationWithErrorResolution(relevanceTimeoutMillis, emptyArray())

    /**
     * Запросить текущее местоположение с решением возникающих проблем.
     *
     * @param relevanceTimeoutMillis таймаут, при котором последнее полученное местоположение актуально.
     */
    fun observeCurrentLocationWithErrorResolution(
            relevanceTimeoutMillis: Long = DEFAULT_RELEVANCE_TIME_MILLIS
    ): Single<Location> = observeCurrentLocationWithErrorResolution(relevanceTimeoutMillis, resolutions)

    /**
     * Проверить возможность получения местоположения.
     *
     * @return [List], содержащий исключения связанные с невозможностью получения местоположения.
     */
    fun checkLocationAvailability() = locationService.checkLocationAvailability()

    private fun observeLastKnownLocationWithErrorResolution(
            resolutions: Array<out LocationErrorResolution<*>>
    ): Maybe<Location> =
            locationService.observeLastKnownLocation(*resolutions)
                    .doOnSuccess {
                        lastCurrentLocation = it
                        lastCurrentLocationRequestTimeMillis = System.currentTimeMillis()
                    }

    private fun observeCurrentLocationWithErrorResolution(
            relevanceTimeoutMillis: Long,
            resolutions: Array<out LocationErrorResolution<*>>
    ): Single<Location> {
        val relevantLastCurrentLocation = getRelevantLastCurrentLocationOrNull(relevanceTimeoutMillis)
        return if (relevantLastCurrentLocation != null) {
            Single.just(relevantLastCurrentLocation)
        } else {
            locationService.observeLocationUpdates(0, 0, LocationPriority.HIGH_ACCURACY, *resolutions)
                    .firstOrError()
                    .doOnSuccess {
                        lastCurrentLocation = it
                        lastCurrentLocationRequestTimeMillis = System.currentTimeMillis()
                    }
        }
    }

    private fun getRelevantLastCurrentLocationOrNull(relevanceTimeoutMillis: Long): Location? {
        val nonNullLastCurrentLocation = lastCurrentLocation ?: return null
        val nonNullRequestTimeMillis = lastCurrentLocationRequestTimeMillis ?: return null
        val currentTimeMillis = System.currentTimeMillis()
        val requestDeltaTimeMillis = currentTimeMillis - nonNullRequestTimeMillis

        return if (requestDeltaTimeMillis > relevanceTimeoutMillis) {
            null
        } else {
            nonNullLastCurrentLocation
        }
    }
}