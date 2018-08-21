package ru.surfstudio.android.location

import com.google.android.gms.location.LocationCallback

/**
 * Подписка на получение обновлений местоположения.
 */
class LocationUpdatesSubscription(internal val locationCallback: LocationCallback)