package ru.surfstudio.android.location.deprecated

import android.location.Location

import androidx.annotation.RequiresPermission

import io.reactivex.Completable

import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.exceptions.CompositeException
import ru.surfstudio.android.location.deprecated.domain.LocationPriority
import ru.surfstudio.android.location.deprecated.exceptions.NoLocationPermissionException
import ru.surfstudio.android.location.deprecated.exceptions.PlayServicesAreNotAvailableException
import ru.surfstudio.android.location.deprecated.exceptions.ResolutionFailedException
import ru.surfstudio.android.location.deprecated.location_errors_resolver.LocationErrorsResolver
import ru.surfstudio.android.location.deprecated.location_errors_resolver.resolutions.LocationErrorResolution
import ru.surfstudio.android.location.deprecated.location_errors_resolver.resolutions.impl.concrete.no_location_permission.NoLocationPermissionResolution
import ru.surfstudio.android.location.deprecated.location_errors_resolver.resolutions.impl.concrete.play_services_are_not_available.PlayServicesAreNotAvailableResolution
import ru.surfstudio.android.location.deprecated.location_errors_resolver.resolutions.impl.concrete.resolveble_api_exception.ResolvableApiExceptionResolution

/**
 * Common interface for LocationService
 */
interface ILocationService {

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
    fun checkLocationAvailability(priority: LocationPriority): Completable

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
    fun observeLastKnownLocation(): Maybe<Location>

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
    ): Observable<Location>
}