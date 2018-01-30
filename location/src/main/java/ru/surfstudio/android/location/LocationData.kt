package ru.surfstudio.android.location

/**
 * Пакет данных геолокации
 */
const val UNKNOWN_PROVIDER = "Unknown"

data class LocationData(val provider: String? = UNKNOWN_PROVIDER,
                        val lon: Double? = MOSCOW_LON,
                        val lat: Double? = MOSCOW_LAT,
                        val time: Long? = 0) {

    fun isEmpty() = provider == null && lon == null && lat == null && time == null

    fun isProviderUnknown() = provider == UNKNOWN_PROVIDER

    fun isLocationDefault() = this == LocationData()
}