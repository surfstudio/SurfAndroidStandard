/*
 * Copyright 2016 Valeriy Shtaits.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.surfstudio.android.location

import android.content.Context
import android.location.Location
import android.support.annotation.RequiresPermission
import com.google.android.gms.location.*
import ru.surfstudio.android.location.domain.LocationPriority
import ru.surfstudio.android.location.location_errors_resolver.LocationErrorsResolver
import ru.surfstudio.android.location.location_errors_resolver.resolutions.LocationErrorResolution

/**
 * Сервис для работы с метоположением.
 */
class LocationService(private val context: Context) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationAvailability = LocationAvailability(context)

    /**
     * Получить последнее известное метоположение.
     *
     * @param resolutions [Array], содержащий решения проблем связанных с невозможностью получения метоположения.
     */
    @RequiresPermission(
            anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"]
    )
    fun getLastKnownLocationWithErrorResolution(
            onSuccessAction: (Location?) -> Unit,
            onFailureAction: (List<Exception>) -> Unit,
            resolutions: List<LocationErrorResolution<*>>
    ) {
        getLastKnownLocation(
                onSuccessAction,
                onFailureAction = { exceptions ->
                    resolveLocationErrors(
                            exceptions,
                            resolutions,
                            onFinishAction = { getLastKnownLocation(onSuccessAction, onFailureAction) },
                            onFailureAction = onFailureAction
                    )
                }
        )
    }

    /**
     * Подписаться на получение обновлений местоположения.
     *
     * @param intervalMillis интервал в миллисекундах, при котором предпочтительно получать обновления местоположения.
     * Тем не менее, обновления местоположения могут быть чаще, чем этот интервал, если другое приложение получает
     * обновления с меньшим интервалом. Или, наоборот, реже (например, если у устройства нет возможности подключения).
     * @param fastestIntervalMillis максимальный интервал в миллисекундах, при котором возможно обрабатывать обновления
     * местоположения. Следует устанавливать этот параметр, потому что другие приложения также влияют на скорость
     * отправки обновлений. Google Play Services отправляют обновления с максимальной скоростью, которую запросило любое
     * приложение. Если этот показатель быстрее, чем может обрабатывать приложение, можно столкнуться с соответствующими
     * проблемами.
     * @param priority Приоритет запроса (точность метостоположения/заряд батареи), который дает Google Play Services
     * знать, какие источники данных использовать.
     * @param resolutions [Array], содержащий решения проблем связанных с невозможностью получения метоположения.
     */
    fun requestLocationUpdatesWithErrorResolution(
            intervalMillis: Long?,
            fastestIntervalMillis: Long?,
            priority: LocationPriority?,
            onLocationUpdateAction: (Location?) -> Unit,
            onFailureAction: (List<Exception>) -> Unit,
            resolutions: List<LocationErrorResolution<*>>
    ): LocationCallback? {
        return requestLocationUpdates(
                intervalMillis,
                fastestIntervalMillis,
                priority,
                onLocationUpdateAction,
                onFailureAction = { exceptions ->
                    resolveLocationErrors(
                            exceptions,
                            resolutions,
                            onFinishAction = {
                                requestLocationUpdates(
                                        intervalMillis,
                                        fastestIntervalMillis,
                                        priority,
                                        onLocationUpdateAction,
                                        onFailureAction)
                            },
                            onFailureAction = onFailureAction
                    )
                }
        )
    }

    /**
     * Отписаться от получения обновлений местоположения.
     */
    fun removeLocationUpdates(locationCallback: LocationCallback) {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    /**
     * Проверить возможность получения метоположения.
     *
     * @return [List], содержащий исключения связанные с невозможностью получения метоположения.
     */
    fun checkCanGetLocation(): List<Exception> = locationAvailability.checkCanGetLocation()

    @RequiresPermission(
            anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"]
    )
    private fun getLastKnownLocation(onSuccessAction: (Location?) -> Unit, onFailureAction: (List<Exception>) -> Unit) {
        val exceptions = locationAvailability.checkCanGetLocation()
        if (exceptions.isNotEmpty()) {
            onFailureAction(exceptions)
            return
        }

        fusedLocationClient
                .lastLocation
                .addOnSuccessListener { location -> onSuccessAction(location) }
                .addOnFailureListener { exception -> onFailureAction(listOf(exception)) }
    }

    @RequiresPermission(
            anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"]
    )
    private fun requestLocationUpdates(
            intervalMillis: Long?,
            fastestIntervalMillis: Long?,
            priority: LocationPriority?,
            onLocationUpdateAction: (Location?) -> Unit,
            onFailureAction: (List<Exception>) -> Unit
    ): LocationCallback? {
        val exceptions = locationAvailability.checkCanGetLocation()
        if (exceptions.isNotEmpty()) {
            onFailureAction(exceptions)
            return null
        }

        val locationRequest = createLocationRequest(intervalMillis, fastestIntervalMillis, priority)
        val locationSettingsRequest = createLocationSettingsRequest(locationRequest)
        val locationCallback = createLocationCallback(onLocationUpdateAction)

        LocationServices
                .getSettingsClient(context)
                .checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener {
                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
                }
                .addOnFailureListener { exception -> onFailureAction(listOf(exception)) }

        return locationCallback
    }

    private fun createLocationRequest(
            intervalMillis: Long?,
            fastestIntervalMillis: Long?,
            priority: LocationPriority?
    ): LocationRequest {
        val locationRequest = LocationRequest()

        if (intervalMillis != null) {
            locationRequest.interval = intervalMillis
        }

        if (fastestIntervalMillis != null) {
            locationRequest.fastestInterval = fastestIntervalMillis
        }

        if (priority != null) {
            locationRequest.priority = locationPriorityToInt(priority)
        }

        return locationRequest
    }

    private fun locationPriorityToInt(locationPriority: LocationPriority) =
            when (locationPriority) {
                LocationPriority.HIGH_ACCURACY -> LocationRequest.PRIORITY_HIGH_ACCURACY
                LocationPriority.BALANCED_POWER_ACCURACY -> LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
                LocationPriority.LOW_POWER -> LocationRequest.PRIORITY_LOW_POWER
                LocationPriority.NO_POWER -> LocationRequest.PRIORITY_NO_POWER
            }

    private fun createLocationSettingsRequest(locationRequest: LocationRequest) =
            LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest)
                    .build()

    private fun createLocationCallback(onLocationUpdateAction: (Location?) -> Unit): LocationCallback =
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult ?: return
                    onLocationUpdateAction(locationResult.lastLocation)
                }
            }

    private fun resolveLocationErrors(
            exceptions: List<Exception>,
            resolutions: List<LocationErrorResolution<*>>,
            onFinishAction: () -> Unit,
            onFailureAction: (List<Exception>) -> Unit
    ) {
        LocationErrorsResolver.resolve(
                exceptions,
                resolutions,
                onFinishAction = { unresolvedExceptions ->
                    if (unresolvedExceptions.isEmpty()) {
                        onFinishAction()
                    } else {
                        onFailureAction(unresolvedExceptions)
                    }
                },
                onFailureAction = { resolvingException ->
                    onFailureAction(listOf(resolvingException))
                }
        )
    }
}