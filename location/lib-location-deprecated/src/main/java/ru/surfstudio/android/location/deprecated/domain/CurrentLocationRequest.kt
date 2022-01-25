package ru.surfstudio.android.location.deprecated.domain

import ru.surfstudio.android.location.deprecated.location_errors_resolver.resolutions.impl.concrete.no_location_permission.LocationPermissionRequest

/**
 * Запрос текущего местоположения.
 *
 * @param priority Приоритет запроса (точность метостоположения/заряд батареи), который дает Google Play Services знать,
 * какие источники данных использовать.
 * @param relevanceTimeoutMillis Таймаут, при котором последнее полученное местоположение актуально. Если
 * местоположение ещё актуально - запрос не инициируется, а сразу возвращается закешированное значение.
 * @param expirationTimeoutMillis Таймаут, по истечении которого выполнение запроса завершается исключением
 * [TimeoutException]. Если задано значение меньшее или равное нулю, то таймаут не устанавливается.
 * @param resolveLocationErrors Нужно ли решать проблемы с получением местоположения.
 * @param locationPermissionRequest Запрос разрешения на доступ к местоположению, используемый в
 * [NoLocationPermissionResolution].
 */
@Deprecated("Prefer using new implementation")
class CurrentLocationRequest(
    val priority: LocationPriority = LocationPriority.BALANCED_POWER_ACCURACY,
    val relevanceTimeoutMillis: Long = 5_000L,
    val expirationTimeoutMillis: Long = 0,
    val resolveLocationErrors: Boolean = true,
    val locationPermissionRequest: LocationPermissionRequest = LocationPermissionRequest()
)