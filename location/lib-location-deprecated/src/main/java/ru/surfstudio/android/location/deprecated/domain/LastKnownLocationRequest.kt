package ru.surfstudio.android.location.deprecated.domain

import ru.surfstudio.android.location.deprecated.location_errors_resolver.resolutions.impl.concrete.no_location_permission.LocationPermissionRequest

/**
 * Запрос последнего известного местоположения.
 *
 * @param priority Приоритет запроса (точность метостоположения/заряд батареи), который дает Google Play Services знать,
 * какие источники данных использовать.
 * @param resolveLocationErrors Нужно ли решать проблемы с получением местоположения.
 * @param locationPermissionRequest Запрос разрешения на доступ к местоположению, используемый в
 * [NoLocationPermissionResolution].
 */
class LastKnownLocationRequest(
    val priority: LocationPriority = LocationPriority.BALANCED_POWER_ACCURACY,
    val resolveLocationErrors: Boolean = true,
    val locationPermissionRequest: LocationPermissionRequest = LocationPermissionRequest()
)