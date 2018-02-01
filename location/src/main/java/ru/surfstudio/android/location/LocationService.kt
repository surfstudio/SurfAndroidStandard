package ru.surfstudio.android.location

import android.content.Context
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.subjects.BehaviorSubject
import ru.surfstudio.android.core.app.scheduler.SchedulersProvider
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Сервис, предоставляющий доступ к геолокации пользователя
 */
class LocationService @Inject constructor(val context: Context,
                                          val schedulersProvider: SchedulersProvider,
                                          val locationProvider: LocationProvider) {

    private val locationDetectionTimeout = 30L              //тайм-аут на запрос геопозиции в секундах
    @Volatile
    private var activeLocationRequestsCount = 0   //количество активных запросов геопозиции
        get() = if (field < 0) 0 else field

    private var locationSubject = BehaviorSubject.create<LocationData>()

    /**
     * Однократное получение геопозиции.
     *
     * При возникновении любой ошибки, в том числе ошибки доступа к геолокационным сервисам -
     * возвращается последняя известная геопозиция, но если если определение геолокации отключено
     * на устройстве или произошла ошибка при запросе последней известной геопозиции - вернутся
     * координаты центра Москвы.
     */
    fun getLocation(timeout: Long = locationDetectionTimeout,
                    shouldReset: Boolean = false,
                    locationOnFail: LocationData = UNKNOWN_LOCATION): Observable<LocationData?> {
        if (shouldReset) reset()

        return createLocationDataObservable(timeout, locationOnFail)
                .doOnDispose { reset() }
                .subscribeOn(schedulersProvider.worker())
    }

    /**
     * Конфигурирация запроса геолокации.
     *
     * Здесь устанавливается тайм-аут, политика обработки ошибок определения геопозиции, логика
     * блокиратора одновременных запросов, освобождение ресурсов по итогам определения.
     */
    @Synchronized
    private fun createLocationDataObservable(timeout: Long, locationOnFail: LocationData = UNKNOWN_LOCATION): Observable<LocationData?> {
        val request = if (activeLocationRequestsCount == 0) {
            Observable.fromCallable { locationProvider.startLocationDetector(LocationProviderListener()) }
                    .flatMap({ locationSubject })
        } else {
            locationSubject
        }
        activeLocationRequestsCount++

        return request
                .onErrorResumeNext { next: Observer<in LocationData?> ->
                    locationProvider.getLastKnownLocation(locationOnFail) { next.onNext(it) }
                }
                .first(locationOnFail)
                .timeout(timeout, TimeUnit.SECONDS)
                .toObservable()
    }

    /**
     * Отмена текущего запроса и подготовка сервиса к новому запросу.
     */
    @Synchronized
    private fun reset() {
        activeLocationRequestsCount--
        if (activeLocationRequestsCount == 0) {
            locationProvider.reset()
            locationSubject = BehaviorSubject.create()
        }
    }

    /**
     * Реализация обработчика результатов определения геопозиции.
     */
    private inner class LocationProviderListener : LocationListener {

        override fun onSuccess(locationData: LocationData) {
            locationSubject.onNext(locationData)
        }

        override fun onError(e: Throwable) {
            locationSubject.onError(e)
        }
    }
}

