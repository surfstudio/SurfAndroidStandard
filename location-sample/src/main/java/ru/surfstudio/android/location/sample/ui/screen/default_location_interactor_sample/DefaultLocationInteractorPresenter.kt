package ru.surfstudio.android.location.sample.ui.screen.default_location_interactor_sample

import android.location.Location
import io.reactivex.*
import io.reactivex.disposables.Disposable
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.location.DefaultLocationInteractor
import ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.no_location_permission.NoLocationPermissionResolution
import ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.play_services_are_not_available.PlayServicesAreNotAvailableResolution
import ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.resolveble_api_exception.ResolvableApiExceptionResolution
import ru.surfstudio.android.location.sample.ui.screen.base.BaseSamplePresenter
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider

/**
 * Презентер экрана [DefaultLocationInteractorActivityView]
 */
class DefaultLocationInteractorPresenter(
        screenEventDelegateManager: ScreenEventDelegateManager,
        screenState: ScreenState,
        schedulersProvider: SchedulersProvider,
        private val defaultLocationInteractor: DefaultLocationInteractor
) : BaseSamplePresenter<DefaultLocationInteractorActivityView>(
        screenEventDelegateManager,
        screenState,
        schedulersProvider
) {

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
        val checkLocationAvailabilityCompletable: Completable = defaultLocationInteractor.checkLocationAvailability()

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
         * Запросить последнее известное местоположение без попыток решения возникающих проблем.
         */
        val lastKnownLocationMaybe: Maybe<Location> = defaultLocationInteractor.observeLastKnownLocation()

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

    fun getLastKnownLocationWithErrorResolution() {
        view.showLoading()

        /**
         * Запросить последнее известное местоположение с попыткой решения возникающих проблем.
         *
         * Применяемые решения:
         * - [NoLocationPermissionResolution]
         * - [PlayServicesAreNotAvailableResolution]
         * - [ResolvableApiExceptionResolution]
         */
        val lastKnownLocationMaybe: Maybe<Location> =
                defaultLocationInteractor.observeLastKnownLocationWithErrorResolution()

        lastKnownLocationMaybe.configureAndSubscribe(
                object : MaybeObserver<Location> {

                    /**
                     * Вызывается в случае удачного получения местоположения.
                     */
                    override fun onSuccess(location: Location) {
                        hideLoadingAndShowLocation(location)
                    }

                    /**
                     * Вызывается, если местоположение было получено, но равно null.
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
                     * - [ResolutionFailedException]
                     */
                    override fun onError(t: Throwable) {
                        hideLoadingAndShowLocationIsNotAvailable(t)
                    }

                    override fun onSubscribe(d: Disposable) {
                    }
                }
        )
    }

    fun getCurrentLocation() {
        view.showLoading()

        /**
         * Запросить текущее местоположение без попытки решения возникающих проблем.
         *
         * Принимает в качестве параметра таймаут, при котором последнее полученное местоположение актуально (по
         * умолчанию 5 секунд). В случае, если местоположение ещё актуально, запрос не инициируется, а сразу
         * возвращается закешированное значение.
         */
        val currentLocationSingle: Single<Location> = defaultLocationInteractor.observeCurrentLocation(10_000)

        currentLocationSingle
                .configureAndSubscribe(
                        object : SingleObserver<Location> {

                            /**
                             * Вызывается в случае удачного получения местоположения.
                             */
                            override fun onSuccess(location: Location) {
                                hideLoadingAndShowLocation(location)
                            }

                            /**
                             * Вызывается в случае ошибки.
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

    fun getCurrentLocationWithErrorResolution() {
        view.showLoading()

        /**
         * Запросить текущее местоположение с попыткой решения возникающих проблем.
         *
         *  Применяемые решения:
         * - [NoLocationPermissionResolution]
         * - [PlayServicesAreNotAvailableResolution]
         * - [ResolvableApiExceptionResolution]
         *
         * Принимает в качестве параметра таймаут, при котором последнее полученное местоположение актуально (по
         * умолчанию 5 секунд). В случае, если местоположение ещё актуально, запрос не инициируется, а сразу
         * возвращается закешированное значение.
         */
        val currentLocationSingle: Single<Location> =
                defaultLocationInteractor.observeCurrentLocationWithErrorResolution(10_000)

        currentLocationSingle
                .configureAndSubscribe(
                        object : SingleObserver<Location> {

                            /**
                             * Вызывается в случае удачного получения местоположения.
                             */
                            override fun onSuccess(location: Location) {
                                hideLoadingAndShowLocation(location)
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
                             * - [ResolutionFailedException]
                             */
                            override fun onError(t: Throwable) {
                                hideLoadingAndShowLocationIsNotAvailable(t)
                            }

                            override fun onSubscribe(d: Disposable) {
                            }
                        }
                )
    }
}