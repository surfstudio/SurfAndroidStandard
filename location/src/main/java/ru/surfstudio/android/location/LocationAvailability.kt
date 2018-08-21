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
import ru.surfstudio.android.location.exceptions.NoLocationPermissionException
import ru.surfstudio.android.location.exceptions.PlayServicesAreNotAvailableException

/**
 * Помощник для проверки возможности получения метоположения.
 */
class LocationAvailability(private val context: Context) {

    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    /**
     * Проверить возможность получения метоположения.
     *
     * @return [List], содержащий исключения связанные с невозможностью получения метоположения.
     */
    fun checkLocationAvailability(): List<Exception> {
        val exceptions = ArrayList<Exception>()

        val connectionResult = getGooglePlayServicesConnection()
        if (connectionResult != ConnectionResult.SUCCESS) {
            exceptions.add(PlayServicesAreNotAvailableException(connectionResult))
        }

        if (!isLocationPermissionGranted()) {
            exceptions.add(NoLocationPermissionException())
        }

        return exceptions
    }

    fun getGooglePlayServicesConnection(): Int =
            GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)

    fun isLocationPermissionGranted() = isFineLocationPermissionGranted() || isCoarseLocationPermissionGranted()

    fun isFineLocationPermissionGranted() = isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)

    fun isCoarseLocationPermissionGranted() = isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)

    fun isGpsEnabled() = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

    private fun isPermissionGranted(permission: String) =
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}