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
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.exceptions.CompositeException
import ru.surfstudio.android.location.domain.LocationPriority
import ru.surfstudio.android.location.exceptions.NoLocationPermissionException
import ru.surfstudio.android.location.exceptions.PlayServicesAreNotAvailableException
import ru.surfstudio.android.location.exceptions.ResolutionFailedException
import ru.surfstudio.android.location.location_errors_resolver.LocationErrorsResolver
import ru.surfstudio.android.location.location_errors_resolver.resolutions.LocationErrorResolution
import ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.no_location_permission.NoLocationPermissionResolution
import ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.play_services_are_not_available.PlayServicesAreNotAvailableResolution
import ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.resolveble_api_exception.ResolvableApiExceptionResolution

/**
 * Сервис для работы с местоположением.
 */
class LocationService(
        context: Context,
        private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
) {

    private val locationAvailability = LocationAvailability(context)

    /**
     * Проверить возможность получения местоположения.
     *
     * @param priority Приоритет при получении местоположения.
     *
     * @return [Completable]:
     * - onComplete() вызывается, если есть возможность получить местоположение;
     * - onError() вызывается, если нет возможности получить местоположение. Приходит [CompositeException], содержащий
     * список из возможных исключений: [NoLocationPermissionException], [PlayServicesAreNotAvailableException],
     * [ResolvableApiException].
     */
    fun checkLocationAvailability(priority: LocationPriority): Completable =
            locationAvailability.checkLocationAvailability(priority)

    /**
     * Решить проблемы связанные с невозможностью получения местоположения.
     *
     * @param throwables [List], содержащий исключения связанные с невозможностью получения местоположения.
     * @param resolutions [Array], содержащий решения проблем связанных с невозможностью получения местоположения.
     * Доступные решения: [NoLocationPermissionResolution], [PlayServicesAreNotAvailableResolution],
     * [ResolvableApiExceptionResolution].
     *
     * @return [Single]:
     * - onSuccess() вызывается при удачном решении проблем. Содержит [List] из нерешенных исключений, для которых не
     * передавались решения;
     * - onError() вызывается в случае, если попытка решения проблем не удалась. Приходит [ResolutionFailedException].
     */
    fun resolveLocationAvailability(
            throwables: List<Throwable>,
            vararg resolutions: LocationErrorResolution<*>
    ): Single<List<Throwable>> = LocationErrorsResolver.resolve(throwables, resolutions.toList())

    /**
     * Запросить последнее известное местоположение.
     *
     * @return [Maybe]:
     * - onSuccess() вызывается в случае удачного получения местоположения;
     * - onComplete() вызывается в случае, если местоположение было получено, но равно null;
     * - onError() вызывается, если нет возможности получить местоположение. Приходит [CompositeException], содержащий
     * список из возможных исключений: [NoLocationPermissionException], [PlayServicesAreNotAvailableException],
     * [ResolvableApiException].
     */
    @RequiresPermission(
            anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"]
    )
    fun observeLastKnownLocation(): Maybe<Location> =
            Maybe.create<Location> { maybeEmitter ->
                fusedLocationClient
                        .lastLocation
                        .addOnSuccessListener { location ->
                            if (location == null) {
                                maybeEmitter.onComplete()
                            } else {
                                maybeEmitter.onSuccess(location)
                            }
                        }
                        .addOnFailureListener { exception -> maybeEmitter.onError(exception) }
            }

    /**
     * Подписаться на получение обновлений местоположения.
     *
     * @param intervalMillis Интервал в миллисекундах, при котором предпочтительно получать обновления местоположения.
     * Тем не менее, обновления местоположения могут быть чаще, чем этот интервал, если другое приложение получает
     * обновления с меньшим интервалом. Или, наоборот, реже (например, если у устройства нет возможности подключения).
     * @param fastestIntervalMillis Максимальный интервал в миллисекундах, при котором возможно обрабатывать обновления
     * местоположения. Следует устанавливать этот параметр, потому что другие приложения также влияют на скорость
     * отправки обновлений. Google Play Services отправляют обновления с максимальной скоростью, которую запросило любое
     * приложение. Если этот показатель быстрее, чем может обрабатывать приложение, можно столкнуться с соответствующими
     * проблемами.
     * @param priority Приоритет запроса (точность метостоположения/заряд батареи), который дает Google Play Services
     * знать, какие источники данных использовать.
     *
     * @return [Observable]:
     * - onNext() вызывается при очередном получении обновления местоположения;
     * - onComplete() никогда не вызывается;
     * - onError() вызывается, если нет возможности получить местоположение. Приходит [CompositeException], содержащий
     * список из возможных исключений: [NoLocationPermissionException], [PlayServicesAreNotAvailableException],
     * [ResolvableApiException].
     */
    @RequiresPermission(
            anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"]
    )
    fun observeLocationUpdates(
            intervalMillis: Long?,
            fastestIntervalMillis: Long?,
            priority: LocationPriority?
    ): Observable<Location> {
        var locationCallback: LocationCallback? = null
        return Observable
                .create<Location> { observableEmitter ->
                    val locationRequest =
                            LocationUtil.createLocationRequest(intervalMillis, fastestIntervalMillis, priority)
                    locationCallback = createLocationCallback(observableEmitter)
                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
                }
                .subscribeOn(AndroidSchedulers.mainThread())
                .doFinally { fusedLocationClient.removeLocationUpdates(locationCallback) }
    }

    private fun createLocationCallback(observableEmitter: ObservableEmitter<Location>): LocationCallback =
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    val nonNullLastLocation = locationResult?.lastLocation ?: return
                    observableEmitter.onNext(nonNullLastLocation)
                }
            }
}