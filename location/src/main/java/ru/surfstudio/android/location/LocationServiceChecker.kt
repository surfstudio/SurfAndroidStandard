package ru.surfstudio.android.location

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import io.reactivex.Observable
import io.reactivex.functions.Function3
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import ru.surfstudio.android.core.app.dagger.scope.PerScreen
import ru.surfstudio.android.core.app.log.Logger
import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider
import ru.surfstudio.android.core.ui.base.delegate.activity.result.ActivityResultDelegate
import ru.surfstudio.android.core.ui.base.delegate.activity.result.SupportOnActivityResultRoute.EXTRA_RESULT
import ru.surfstudio.android.core.ui.base.navigation.activity.route.ActivityWithResultRoute
import ru.surfstudio.android.core.ui.base.permission.PermissionManager
import ru.surfstudio.android.location.storage.LocationPermissionStorage
import ru.trinitydigital.poison.interactor.location.LocationPermissionRequest
import java.lang.Exception
import javax.inject.Inject

/**
 * Утилита для проверки доступности геолокационного сервиса.
 *
 * Проверяет:
 * * наличие на устройстве установленных Google Play Services;
 * * разрешение на доступ к геолокационному сервису у приложения;
 * * включённость GPS на устройстве.
 */
@PerScreen
class LocationServiceChecker @Inject constructor(private val activityProvider: ActivityProvider,
                                                 private val locationPermissionRoute: ActivityWithResultRoute<Boolean>,
                                                 private val locationPermissionManager: PermissionManager)
    : ActivityResultDelegate {

    private val requestGooglePlayServices = 201
    private val requestCustomPermissionDialogCode = 123
    private val requestEnableLocation = Math.abs(this.hashCode() % 32768)
    private val locationPermissionStorage = LocationPermissionStorage(activityProvider.get().applicationContext)
    private val locationEnablePublishSubject = PublishSubject.create<Boolean>()
    private val checkPlayServicesResultSubject = BehaviorSubject.create<Boolean>()
    private val permissionsPublishSubject = BehaviorSubject.create<Boolean>()
    private var googleApiClient: GoogleApiClient = GoogleApiClient.Builder(activityProvider.get().applicationContext)
            .addApi(LocationServices.API)
            .build()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {

        when (requestCode) {
            requestGooglePlayServices -> {
                checkPlayServicesResultSubject.onNext(resultCode == Activity.RESULT_OK)
                return true
            }
            requestEnableLocation -> {
                locationEnablePublishSubject.onNext(resultCode == Activity.RESULT_OK)
                return true
            }
            requestCustomPermissionDialogCode -> {
                val customDialogResult = data?.extras?.getBoolean(EXTRA_RESULT) ?: false
                permissionsPublishSubject.onNext((resultCode == Activity.RESULT_OK) && customDialogResult)
                return true
            }

            else -> return false
        }
    }

    /**
     * Тихая проверка доступности геолокационного сервиса без диалогов и попыток разрешения проблем
     */
    fun isLocationServiceAvailable(): Observable<Boolean> {
        return checkAndResolveLocationServiceAvailability(false)
    }

    /**
     * Тихая проверка статуса пермишнов на доступ к геолокационному сервису
     */
    fun areLocationServicePermissionsGranted(): Boolean {
        return locationPermissionManager.check(LocationPermissionRequest())
    }

    /**
     * Проверка доступности геолокационного сервиса с разрешением проблем при необходимости.
     *
     * Проверяется три пункта:
     * * наличие на устройстве установленных Google Play Services;
     * * разрешение на доступ к геолокационному сервису у приложения;
     * * включённость GPS на устройстве.
     *
     * При расхождению хотя бы по одному из пунктов - пользователь информируется об этом и ему
     * предлагается решить проблему соответствующим образом.
     *
     * @param tryToResolve попытаться ли разрешить проблемы. Если true - то будет запрашиваться
     * пермишн и показываться диалог ошибки в случае отсутствия Google Play Services на девайсе, а
     * также системный диалог включения GPS
     *
     * @return эмитит LocationPermissionRequestResult c isServiceAvailable == true, если геолокация доступна,
     * и с isServiceAvailable == false в противном случае
     */
    fun checkAndResolveLocationServiceAvailability(tryToResolve: Boolean = true): Observable<Boolean> {
        Logger.d("checkAndResolveLocationServiceAvailability")
        checkAndResolveGooglePlayServices(tryToResolve)
        checkAndResolveLocationAvailability(tryToResolve)
        checkAndResolveLocationPermission(tryToResolve)
        return Observable.zip(
                permissionsPublishSubject,
                checkPlayServicesResultSubject,
                locationEnablePublishSubject,
                Function3 { permissionGranted: Boolean, playServicesAvailable: Boolean, locationEnabled: Boolean ->
                    Logger.i("Статусы доступа к геолокации: permissionGranted = " + permissionGranted
                            + ", playServicesAvailable = " + playServicesAvailable +
                            ", locationEnabled = " + locationEnabled)
                    permissionGranted && playServicesAvailable && locationEnabled
                })
                .first(false).toObservable()
    }

    /**
     * Тихая проверка включенности геолокации на устройсте.
     * Не выводит диалоги.
     */
    fun observeLocationEnabled(): Observable<Boolean> {
        checkAndResolveLocationServiceAvailability(false)
        return locationEnablePublishSubject
    }

    /**
     * Проверка наличия на устройстве установленных Google Play Services
     *
     * @param tryToResolve показывать ли диалог при ошибке
     */
    private fun checkAndResolveLocationPermission(tryToResolve: Boolean = true) {
        if (tryToResolve) {
            locationPermissionManager.request(LocationPermissionRequest()).subscribe({ result ->
                //true если стандартнй диалог можно еще показывать
                val showRequestPermissionRationale = locationPermissionManager.shouldShowRequestPermissionRationale(LocationPermissionRequest())

                if (showRequestPermissionRationale) {
                    locationPermissionStorage.saveShowDialogValue(true)
                }

                val needToShowDialog = locationPermissionStorage.getShowDialogValue()

                //срабатывает, если пользователь не дал разрешение в стандартном диалоге и запретил его показывать
                if (!result && !showRequestPermissionRationale && needToShowDialog) {
                    activityProvider.get().startActivityForResult(locationPermissionRoute.prepareIntent(activityProvider.get()), requestCustomPermissionDialogCode)
                } else {
                    permissionsPublishSubject.onNext(result)
                }
            })
        } else {
            permissionsPublishSubject.onNext(locationPermissionManager.check(LocationPermissionRequest()))
        }
    }

    /**
     * Проверка включённости определения геолокации в системе на устройстве
     *
     * @param tryToResolve показывать ли диалог включения геолокации при необходимости
     */
    private fun checkAndResolveLocationAvailability(tryToResolve: Boolean = true) {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(LocationRequest())
        val client = LocationServices.getSettingsClient(activityProvider.get())
        val task = client.checkLocationSettings(builder.build())

        if (!this.googleApiClient.isConnected) {
            this.googleApiClient.connect()
            this.googleApiClient.registerConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                override fun onConnected(bundle: Bundle?) {
                    setLocationEnableResultCallback(task, tryToResolve)
                }

                override fun onConnectionSuspended(i: Int) {
                    locationEnablePublishSubject.onNext(false)
                }
            })
        } else {
            setLocationEnableResultCallback(task, tryToResolve)
        }
    }

    /**
     * Обработка результатов включения доступа к геолокации в настройках устройства
     */
    private fun setLocationEnableResultCallback(task: Task<LocationSettingsResponse>, tryToResolve: Boolean = true) {
        task.addOnSuccessListener({
            locationEnablePublishSubject.onNext(true)
        })

        task.addOnFailureListener({ exception: Exception ->
            val statusCode = (exception as ApiException).statusCode
            when (statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                    locationEnablePublishSubject.onNext(true)
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    if (tryToResolve) {
                        try {
                            val resolvable = (exception as ResolvableApiException)
                            resolvable.startResolutionForResult(activityProvider.get(), requestEnableLocation)
                        } catch (e: IntentSender.SendIntentException) {
                            Logger.e(e, "Ошибка при попытке включить локацию через настройки устройства")
                        }
                    } else {
                        locationEnablePublishSubject.onNext(false)
                    }
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    locationEnablePublishSubject.onNext(false)
                }
                else -> {
                    locationEnablePublishSubject.onNext(false)
                }
            }
        })
    }

    /**
     * Проверка наличия на устройстве установленных Google Play Services
     *
     * @param showErrorDialog показывать ли диалог при ошибке
     */
    private fun checkAndResolveGooglePlayServices(showErrorDialog: Boolean = true) {
        val api = GoogleApiAvailability.getInstance()
        val code = api.isGooglePlayServicesAvailable(activityProvider.get())
        if (code == ConnectionResult.SUCCESS) {
            Logger.i("Google Play Services доступны")
            checkPlayServicesResultSubject.onNext(true)
        } else if (showErrorDialog && api.isUserResolvableError(code) &&
                api.showErrorDialogFragment(activityProvider.get(), code, requestGooglePlayServices)) {
            Logger.i("Google Play Services недоступны")
            checkPlayServicesResultSubject.onNext(false)
            //wait for handleIntent call (see below)
        } else {
            Logger.i("Google Play Services недоступны")
            checkPlayServicesResultSubject.onNext(false)
        }
    }

    fun isAvailable(locationProvider: LocationProvider): Boolean {
        return locationProvider.isProviderAvailable()
    }
}