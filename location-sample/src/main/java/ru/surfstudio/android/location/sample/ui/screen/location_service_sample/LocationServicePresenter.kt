package ru.surfstudio.android.location.sample.ui.screen.location_service_sample

import android.location.Location
import io.reactivex.*
import io.reactivex.disposables.Disposable
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.permission.PermissionManager
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.location.LocationService
import ru.surfstudio.android.location.domain.LocationPriority
import ru.surfstudio.android.location.location_errors_resolver.resolutions.LocationErrorResolution
import ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.no_location_permission.NoLocationPermissionResolution
import ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.play_services_are_not_available.PlayServicesAreNotAvailableResolution
import ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.resolveble_api_exception.ResolvableApiExceptionResolution
import ru.surfstudio.android.location.sample.ui.screen.base.BaseSamplePresenter
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider

/**
 * Презентер экрана [LocationServiceActivityView]
 */
class LocationServicePresenter(
        screenEventDelegateManager: ScreenEventDelegateManager,
        screenState: ScreenState,
        permissionManager: PermissionManager,
        activityProvider: ActivityProvider,
        schedulersProvider: SchedulersProvider,
        private val locationService: LocationService
) : BaseSamplePresenter<LocationServiceActivityView>(screenEventDelegateManager, screenState, schedulersProvider) {

    private val resolutions = mutableListOf<LocationErrorResolution<*>>()
    private val noLocationPermissionResolution =
            NoLocationPermissionResolution(permissionManager)
    private val playServicesAreNotAvailableResolution =
            PlayServicesAreNotAvailableResolution(screenEventDelegateManager, activityProvider)
    private val resolvableApiExceptionResolution =
            ResolvableApiExceptionResolution(screenEventDelegateManager, activityProvider)

    private var locationUpdatesDisposable: Disposable? = null

    fun getLocationAvailability() {
        view.showLoading()

        /**
         * Проверить возможность получения местоположения.
         *
         * Возвращается список исключений, связанных с невозможностью получения местоположения. Если список пуст -
         * значит есть возможность получить местоположение.
         *
         * Возможные исключения:
         * - [NoLocationPermissionException]
         * - [PlayServicesAreNotAvailableException]
         */
        val checkLocationAvailabilityCompletable: Completable =
                locationService.checkLocationAvailability(LocationPriority.HIGH_ACCURACY)

        checkLocationAvailabilityCompletable
                .configureAndSubscribe(
                        object : CompletableObserver {

                            /**
                             * Вызывается, если есть возомжность получить местоположение.
                             */
                            override fun onComplete() {
                                hideLoadingAndShowLocationIsAvailable()
                            }

                            /**
                             * Вызывается, если нет возможности получить местоположение.
                             *
                             * Может прийти [CompositeException], содержащий список из возможных исключений:
                             * [NoLocationPermissionException], [PlayServicesAreNotAvailableException],
                             * [ResolvableApiException]
                             */
                            override fun onError(t: Throwable) {
                                hideLoadingAndShowLocationIsNotAvailable(t)
                            }

                            override fun onSubscribe(d: Disposable) {
                            }
                        }
                )

    }

    fun getLastKnownLocation() {
        view.showLoading()

        /**
         * Запросить последнее известное местоположение.
         *
         * Принимает в качестве параметра массив решений проблем связанных с невозможностью получения местоположения.
         * Доступные решения:
         * - [NoLocationPermissionResolution]
         * - [PlayServicesAreNotAvailableResolution]
         * - [ResolvableApiExceptionResolution]
         */
        val lastKnownLocationMaybe: Maybe<Location> =
                locationService.observeLastKnownLocation(*resolutions.toTypedArray())

        lastKnownLocationMaybe.configureAndSubscribe(
                object : MaybeObserver<Location> {

                    /**
                     * Вызывается в случае удачного получения местоположения.
                     */
                    override fun onSuccess(location: Location) {
                        hideLoadingAndShowLocation(location)
                    }

                    /**
                     * Вызывается в случае, если местоположение было получено, но равно null.
                     */
                    override fun onComplete() {
                        hideLoadingAndShowNoLocation()
                    }

                    /**
                     * Вызывается в случае ошибки.
                     *
                     * Могут прийти следующие исключения:
                     *
                     * - [CompositeException], содержащий список из возможных исключений:
                     * [NoLocationPermissionException], [PlayServicesAreNotAvailableException],
                     * [ResolvableApiException]
                     *
                     * - [ResolutionFailedException], если передавались экземпляры решений и попытка решения не удалась
                     */
                    override fun onError(t: Throwable) {
                        hideLoadingAndShowLocationIsNotAvailable(t)
                    }

                    override fun onSubscribe(d: Disposable) {
                    }
                }
        )
    }

    fun subscribeToLocationUpdates() {
        /**
         * Подписаться на получение обновлений местоположения.
         *
         * Принимает в качестве параметров:
         * - интервал в миллисекундах, при котором предпочтительно получать обновления местоположения;
         * - максимальный интервал в миллисекундах, при котором возможно обрабатывать обновления местоположения;
         * - приоритет запроса (точность метостоположения/заряд батареи);
         * - массив решений проблем связанных с невозможностью получения местоположения. Доступные решения:
         *   - [NoLocationPermissionResolution]
         *   - [PlayServicesAreNotAvailableResolution]
         *   - [ResolvableApiExceptionResolution]
         */
        val locationUpdatesObservable: Observable<Location> =
                locationService.observeLocationUpdates(
                        1000,
                        1000,
                        LocationPriority.HIGH_ACCURACY,
                        *resolutions.toTypedArray()
                )

        locationUpdatesObservable
                .configureAndSubscribe(
                        object : Observer<Location> {

                            /**
                             * Вызывается при каждом удачном получении местоположения
                             */
                            override fun onNext(location: Location) {
                                view.showLocation(location)
                            }

                            /**
                             * Никогда не вызывается
                             */
                            override fun onComplete() {
                            }

                            /**
                             * Вызывается в случае ошибки.
                             *
                             * Могут прийти следующие исключения:
                             *
                             * - [CompositeException], содержащий список из возможных исключений:
                             * [NoLocationPermissionException], [PlayServicesAreNotAvailableException],
                             * [ResolvableApiException]
                             *
                             * - [ResolutionFailedException], если передавались экземпляры решений и попытка решения
                             * не удалась
                             */
                            override fun onError(t: Throwable) {
                                view.showLocationIsNotAvailable(t)
                            }

                            override fun onSubscribe(d: Disposable) {
                                locationUpdatesDisposable = d
                            }
                        }
                )
    }

    fun unsubscribeFromLocationUpdates() {
        locationUpdatesDisposable?.dispose()
    }

    fun addNoLocationPermissionResolution() {
        resolutions.add(noLocationPermissionResolution)
    }

    fun removeNoLocationPermissionResolution() {
        resolutions.remove(noLocationPermissionResolution)
    }

    fun addPlayServicesAreNotAvailableResolution() {
        resolutions.add(playServicesAreNotAvailableResolution)
    }

    fun removePlayServicesAreNotAvailableResolution() {
        resolutions.remove(playServicesAreNotAvailableResolution)
    }

    fun addResolvableApiExceptionResolution() {
        resolutions.add(resolvableApiExceptionResolution)
    }

    fun removeResolvableApiExceptionResolution() {
        resolutions.remove(resolvableApiExceptionResolution)
    }
}