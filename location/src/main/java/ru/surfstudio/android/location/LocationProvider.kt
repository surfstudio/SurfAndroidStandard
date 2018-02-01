package ru.surfstudio.android.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.support.v4.content.ContextCompat
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import ru.surfstudio.android.core.app.dagger.scope.PerApplication
import ru.surfstudio.android.core.app.log.Logger
import ru.surfstudio.android.location.error.LocationSecurityException
import ru.surfstudio.android.location.error.LocationServiceUnavailableException
import javax.inject.Inject

/**
 * Обёртка над Google Play Services. Предоставляет удобный интерфейс для получения данных о
 * текущей геопозиции пользователя.
 */
@PerApplication
class LocationProvider @Inject constructor(private val appContext: Context) {

    var gpsPriority = LocationRequest.PRIORITY_LOW_POWER
        set(value) {
            field = value
            locationRequest.priority = gpsPriority
        }

    private val networkProvider = LocationManager.NETWORK_PROVIDER
    private val gpsProvider = LocationManager.GPS_PROVIDER
    private val gpsNumUpdates = 1

    private val locationManager: LocationManager by lazy {
        (appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager)
    }

    private val locationRequest: LocationRequest by lazy {
        val locationRequest = LocationRequest()
        locationRequest.numUpdates = gpsNumUpdates
        locationRequest.priority = gpsPriority
        locationRequest
    }

    private val googleApiClient: GoogleApiClient by lazy {
        GoogleApiClient.Builder(appContext)
                .addApi(LocationServices.API)
                .build()
    }
    private var googleLocationCallback: GoogleLocationCallback? = null

    /**
     * Инициализация и запуск детектора геопозиции.
     *
     * @param locationListener реализация @see LocationListener, методы которого вызываются
     * при успешном определении геопозиции или при ошибке
     */
    fun startLocationDetector(locationListener: LocationListener) {
        googleApiClient.blockingConnect()
        this.googleLocationCallback = GoogleLocationCallback(locationListener)
        if (!isLocationAvailable(locationListener)) return
        setLocationProviderCallback()
    }

    /**
     * Получение последней известной геопозиции пользователя.
     *
     * @param lastKnownLocationCallback лямбда, которая срабатывает при определении последней
     * доступной геопозиции и возвращает её
     */
    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(locationOnFail: LocationData = UNKNOWN_LOCATION, lastKnownLocationCallback: (LocationData) -> Unit) {
        if (!areLocationPermissionsGranted()) {
            Logger.e("Последняя известная локация недоступна без предоставленных " +
                    "пермишнов ACCESS_COARSE_LOCATION или ACCESS_FINE_LOCATION.")
            lastKnownLocationCallback(locationOnFail)
            return
        }
        val lastKnownLocationTask = LocationServices.getFusedLocationProviderClient(appContext).lastLocation
        lastKnownLocationTask.addOnSuccessListener { lastKnownLocation: Location? ->
            val locationData: LocationData =
                    if (lastKnownLocation == null) {
                        locationOnFail
                    } else {
                        LocationData(
                                lastKnownLocation.provider,
                                lastKnownLocation.longitude,
                                lastKnownLocation.latitude,
                                lastKnownLocation.time)
                    }
            lastKnownLocationCallback(locationData)
        }
        lastKnownLocationTask.addOnFailureListener { exception ->
            Logger.e(exception, "Ошибка получения последней известной локации.")
            lastKnownLocationCallback(locationOnFail)
        }
    }

    /**
     * Проверка доступности хотя бы одного источника геолокационных данных: GPS или Network
     */
    fun isProviderAvailable(): Boolean {
        return isGpsProviderEnabled() || isNetworkProviderEnabled()
    }

    /**
     * Освобождает все ресурсы, задействованные при запросе геолокации.
     * Следует вызвать при завершении запроса локации или для завершения запроса локации.
     */
    fun reset() {
        if (googleApiClient.isConnected) {
            LocationServices.getFusedLocationProviderClient(appContext)
                    .removeLocationUpdates(googleLocationCallback)
        }
        googleApiClient.disconnect()
    }

    @SuppressLint("MissingPermission")
    private fun setLocationProviderCallback() {
        LocationServices.getFusedLocationProviderClient(appContext)
                .requestLocationUpdates(locationRequest,
                        this.googleLocationCallback,
                        Looper.getMainLooper())
    }

    private fun isLocationAvailable(locationListener: LocationListener): Boolean {
        if (!areLocationPermissionsGranted()) {
            locationListener.onError(LocationSecurityException())
            return false
        }
        if (!isProviderAvailable()) {
            locationListener.onError(LocationServiceUnavailableException())
            return false
        }
        return true
    }

    /**
     * Проверка на доступность GPS-провайдера геолокации
     */
    private fun isGpsProviderEnabled(): Boolean {
        return locationManager.isProviderEnabled(gpsProvider)
    }

    /**
     * Проверка на доступность Network-провайдера геолокации
     */
    private fun isNetworkProviderEnabled(): Boolean {
        return locationManager.isProviderEnabled(networkProvider)
    }

    /**
     * Проверка пермишнов на доступ к геолокации
     */
    private fun areLocationPermissionsGranted(): Boolean {
        return checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
                checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    /**
     * Локальная проверка пермишнов
     */
    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(appContext, permission) ==
                PackageManager.PERMISSION_GRANTED
    }

    /**
     * Функция обратного вызова, срабатывающая при определении геопозиции
     */
    private inner class GoogleLocationCallback(private val locationCallback: LocationListener)
        : LocationCallback() {

        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val location = locationResult.lastLocation
            locationCallback.onSuccess(LocationData(
                    location.provider,
                    location.longitude,
                    location.latitude,
                    location.time))
        }
    }

}