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

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.support.v4.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import ru.surfstudio.android.location.domain.LocationPriority
import ru.surfstudio.android.location.exceptions.NoLocationPermissionException
import ru.surfstudio.android.location.exceptions.PlayServicesAreNotAvailableException

/**
 * Помощник для проверки возможности получения местоположения.
 */
internal class LocationAvailability(private val context: Context) {

    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    /**
     * Проверить возможность получения местоположения.
     *
     * @param priority приоритет при получении местоположения.
     *
     * @param onResultAction метод обратного вызова, в который передается [List], содержащий исключения, связанные с
     * невозможностью получения местоположения. Если список пуст - значит есть возможность получить местоположение.
     * Возможные исключения:
     * - [NoLocationPermissionException];
     * - [PlayServicesAreNotAvailableException];
     * - [ResolvableApiException].
     */
    fun checkLocationAvailability(priority: LocationPriority?, onResultAction: (List<Exception>) -> Unit) {
        val exceptions = arrayListOf<Exception>()

        val connectionResult = getGooglePlayServicesConnection()
        if (connectionResult != ConnectionResult.SUCCESS) {
            exceptions.add(PlayServicesAreNotAvailableException(connectionResult))
        }

        if (!isLocationPermissionGrantedForPriority(priority)) {
            exceptions.add(NoLocationPermissionException())
        }

        checkLocationSettings(priority, onResultAction = { exception ->
            if (exception != null) {
                exceptions.add(exception)
            }
            onResultAction(exceptions)
        })
    }

    /**
     * Проверить, что Google Play Services установлены, включены и версия не меньше той, которая требуется клиенту.
     *
     * @return результат подключения {@link ConnectionResult}. Может быть одним из следующих значений: SUCCESS,
     * SERVICE_MISSING, SERVICE_UPDATING, SERVICE_VERSION_UPDATE_REQUIRED, SERVICE_DISABLED, SERVICE_INVALID
     */
    fun getGooglePlayServicesConnection(): Int =
            GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)

    /**
     * Проверить, выдано ли разрешение на доступ к местоположению для соответствующего [LocationPriority]
     */
    fun isLocationPermissionGrantedForPriority(priority: LocationPriority?) =
            if (priority == LocationPriority.HIGH_ACCURACY) {
                isFineLocationPermissionGranted()
            } else {
                isLocationPermissionGranted()
            }

    /**
     * Проверить, выдано ли разрешение на доступ к местоположению.
     */
    fun isLocationPermissionGranted() = isFineLocationPermissionGranted() || isCoarseLocationPermissionGranted()

    /**
     * Проверить, выдано ли разрешение ACCESS_FINE_LOCATION.
     */
    fun isFineLocationPermissionGranted() = isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)

    /**
     * Проверить, выдано ли разрешение ACCESS_COARSE_LOCATION.
     */
    fun isCoarseLocationPermissionGranted() = isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)

    /**
     * Проверить, включён ли хотя бы один из поставщиков местоположения.
     */
    fun isAnyLocationProviderEnabled() = isGpsProviderEnabled() || isNetworkProviderEnabled()

    /**
     * Проверить, включён ли GPS.
     */
    fun isGpsProviderEnabled() = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

    /**
     * Проверить, включена ли сеть.
     */
    fun isNetworkProviderEnabled() = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

    private fun isPermissionGranted(permission: String) =
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    private fun checkLocationSettings(priority: LocationPriority?, onResultAction: (Exception?) -> Unit) {
        val locationRequest = LocationUtil.createLocationRequest(priority = priority)
        val locationSettingsRequest = createLocationSettingsRequest(locationRequest)

        LocationServices
                .getSettingsClient(context)
                .checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener { _ -> onResultAction(null) }
                .addOnFailureListener { exception -> onResultAction(exception) }
    }

    private fun createLocationSettingsRequest(locationRequest: LocationRequest) =
            LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest)
                    .setAlwaysShow(false)
                    .build()
}