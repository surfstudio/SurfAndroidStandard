package ru.surfstudio.android.location

/**
 * Пакет данных геолокации
 */
const val UNKNOWN_PROVIDER = "Unknown"
val UNKNOWN_LOCATION = LocationData(UNKNOWN_PROVIDER, 0.0, 0.0, 0)

data class LocationData(val provider: String,
                        val lon: Double,
                        val lat: Double,
                        val time: Long) {
    fun isProviderUnknown() = provider == UNKNOWN_PROVIDER
}
