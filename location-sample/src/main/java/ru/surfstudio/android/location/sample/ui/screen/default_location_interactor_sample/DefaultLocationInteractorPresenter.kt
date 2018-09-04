package ru.surfstudio.android.location.sample.ui.screen.default_location_interactor_sample

import android.location.Location
import io.reactivex.*
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.location.DefaultLocationInteractor
import ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.no_location_permission.NoLocationPermissionResolution
import ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.play_services_are_not_available.PlayServicesAreNotAvailableResolution
import ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.resolveble_api_exception.ResolvableApiExceptionResolution
import ru.surfstudio.android.location.sample.ui.screen.base.BaseSamplePresenter

/**
 * Презентер экрана [DefaultLocationInteractorActivityView]
 */
class DefaultLocationInteractorPresenter(
        basePresenterDependency: BasePresenterDependency,
        private val defaultLocationInteractor: DefaultLocationInteractor
) : BaseSamplePresenter<DefaultLocationInteractorActivityView>(basePresenterDependency) {

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

        subscribeIo(
                checkLocationAvailabilityCompletable,

                /**
                 * onComplete() вызывается, если есть возомжность получить местоположение.
                 */
                { hideLoadingAndShowLocationIsAvailable() },

                /**
                 * onError() вызывается, если нет возможности получить местоположение.
                 *
                 * Может прийти [CompositeException], содержащий список из возможных исключений:
                 * [NoLocationPermissionException], [PlayServicesAreNotAvailableException],
                 * [ResolvableApiException]
                 */
                { t: Throwable -> hideLoadingAndShowLocationIsNotAvailable(t) }
        )
    }

    fun getLastKnownLocation() {
        view.showLoading()

        /**
         * Запросить последнее известное местоположение без попыток решения возникающих проблем.
         */
        val lastKnownLocationMaybe: Maybe<Location> = defaultLocationInteractor.observeLastKnownLocation()

        subscribeIo(
                lastKnownLocationMaybe,

                /**
                 * onSuccess() вызывается в случае удачного получения местоположения.
                 */
                { location: Location -> hideLoadingAndShowLocation(location) },

                /**
                 * onComplete() вызывается в случае, если местоположение было получено, но равно null.
                 */
                { hideLoadingAndShowNoLocation() },

                /**
                 * onError() вызывается в случае ошибки.
                 *
                 * Может прийти [CompositeException], содержащий список из возможных исключений:
                 * [NoLocationPermissionException], [PlayServicesAreNotAvailableException],
                 * [ResolvableApiException]
                 */
                { t: Throwable -> hideLoadingAndShowLocationIsNotAvailable(t) }
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

        subscribeIo(
                lastKnownLocationMaybe,

                /**
                 * onSuccess() вызывается в случае удачного получения местоположения.
                 */
                { location: Location -> hideLoadingAndShowLocation(location) },

                /**
                 * onComplete() вызывается, если местоположение было получено, но равно null.
                 */
                { hideLoadingAndShowNoLocation() },

                /**
                 * onError() вызывается в случае ошибки.
                 *
                 * Могут прийти следующие исключения:
                 *
                 * - [CompositeException], содержащий список из возможных исключений:
                 * [NoLocationPermissionException], [PlayServicesAreNotAvailableException],
                 * [ResolvableApiException]
                 *
                 * - [ResolutionFailedException]
                 */
                { t: Throwable -> hideLoadingAndShowLocationIsNotAvailable(t) }
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

        subscribeIo(
                currentLocationSingle,

                /**
                 * onSuccess() вызывается в случае удачного получения местоположения.
                 */
                { location: Location -> hideLoadingAndShowLocation(location) },

                /**
                 * onError() вызывается в случае ошибки.
                 *
                 * Может прийти [CompositeException], содержащий список из возможных исключений:
                 * [NoLocationPermissionException], [PlayServicesAreNotAvailableException],
                 * [ResolvableApiException]
                 */
                { t: Throwable -> hideLoadingAndShowLocationIsNotAvailable(t) }
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

        subscribeIo(
                currentLocationSingle,

                /**
                 * onSuccess() вызывается в случае удачного получения местоположения.
                 */
                { location: Location -> hideLoadingAndShowLocation(location) },

                /**
                 * onError() вызывается в случае ошибки.
                 *
                 * Могут прийти следующие исключения:
                 *
                 * - [CompositeException], содержащий список из возможных исключений:
                 * [NoLocationPermissionException], [PlayServicesAreNotAvailableException],
                 * [ResolvableApiException]
                 *
                 * - [ResolutionFailedException]
                 */
                { t: Throwable -> hideLoadingAndShowLocationIsNotAvailable(t) }
        )
    }
}