package ru.surfstudio.android.location.sample.ui.screen.default_location_interactor_sample

import android.location.Location
import io.reactivex.*
import io.reactivex.exceptions.CompositeException
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.location.DefaultLocationInteractor
import ru.surfstudio.android.location.exceptions.NoLocationPermissionException
import ru.surfstudio.android.location.exceptions.PlayServicesAreNotAvailableException
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

    fun checkLocationAvailability() {
        view.showLoading()

        /**
         * Проверить возможность получения местоположения.
         */
        val checkLocationAvailabilityCompletable: Completable = defaultLocationInteractor.checkLocationAvailability()

        subscribeIo(
                checkLocationAvailabilityCompletable,

                /**
                 * onComplete() вызывается, если есть возможность получить местоположение.
                 */
                { hideLoadingAndShowLocationIsAvailable() },

                /**
                 * onError() вызывается, если нет возможности получить местоположение.
                 *
                 * Приходит [CompositeException], содержащий список из возможных исключений:
                 * [NoLocationPermissionException], [PlayServicesAreNotAvailableException], [ResolvableApiException].
                 */
                { t: Throwable -> hideLoadingAndShowLocationIsNotAvailable(t) }
        )
    }

    fun resolveLocationAvailability() {
        view.showLoading()

        /**
         * Решить проблемы связанные с невозможностью получения местоположения.
         */
        val resolveLocationAvailabilityCompletable = defaultLocationInteractor.resolveLocationAvailability()

        subscribeIo(
                resolveLocationAvailabilityCompletable,

                /**
                 * onComplete() вызывается при удачном решении проблем.
                 */
                { hideLoadingAndShowLocationIsAvailable() },

                /**
                 * onError() вызывается в случае, если попытка решения проблем не удалась. Приходит
                 * [ResolitionFailedException].
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
                 * onError() вызывается, если нет возможности получить местоположение.
                 * Приходит [CompositeException], содержащий список из возможных исключений:
                 * [NoLocationPermissionException], [PlayServicesAreNotAvailableException], [ResolvableApiException].
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
                 * onError() вызывается, если нет возможности получить местоположение.
                 *
                 * Могут прийти следующие исключения:
                 * - [CompositeException], содержащий список из возможных исключений:
                 * [NoLocationPermissionException], [PlayServicesAreNotAvailableException], [ResolvableApiException];
                 * - [ResolutionFailedException].
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
                 * onError() вызывается, если нет возможности получить местоположение.
                 *
                 * Приходит [CompositeException], содержащий список из возможных исключений:
                 * [NoLocationPermissionException], [PlayServicesAreNotAvailableException], [ResolvableApiException].
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
                 * onError() вызывается, если нет возможности получить местоположение.
                 * Могут прийти следующие исключения:
                 * - [CompositeException], содержащий список из возможных исключений:
                 * [NoLocationPermissionException], [PlayServicesAreNotAvailableException], [ResolvableApiException];
                 * - [ResolutionFailedException].
                 */
                { t: Throwable -> hideLoadingAndShowLocationIsNotAvailable(t) }
        )
    }
}