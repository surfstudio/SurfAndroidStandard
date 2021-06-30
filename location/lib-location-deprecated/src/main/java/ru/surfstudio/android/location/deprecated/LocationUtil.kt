package ru.surfstudio.android.location.deprecated

import com.google.android.gms.location.LocationRequest
import ru.surfstudio.android.location.deprecated.domain.LocationPriority

/**
 * Утилита для работы с местоположением.
 */
@Deprecated("Prefer using new implementation")
internal object LocationUtil {

    fun createLocationRequest(
            intervalMillis: Long? = null,
            fastestIntervalMillis: Long? = null,
            priority: LocationPriority? = null
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
}