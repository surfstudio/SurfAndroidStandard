package ru.surfstudio.android.location


/**
 * Интерфейс детектора геолокации
 */
interface LocationListener {
    fun onSuccess(locationData: LocationData)
    fun onError(e: Throwable)
}