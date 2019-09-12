package ru.surfstudio.android.location.sample.ui.screen.default_location_interactor_sample

import android.location.Location
import io.reactivex.*
import io.reactivex.exceptions.CompositeException
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.location.DefaultLocationInteractor
import ru.surfstudio.android.location.domain.CurrentLocationRequest
import ru.surfstudio.android.location.domain.LastKnownLocationRequest
import ru.surfstudio.android.location.domain.LocationPriority
import ru.surfstudio.android.location.sample.ui.screen.common.BaseSamplePresenter
import ru.surfstudio.android.location.sample.ui.screen.common.CommonLocationPermissionRequest

/**
 * Презентер экрана [DefaultLocationInteractorActivityView]
 */
class DefaultLocationInteractorPresenter(
        basePresenterDependency: BasePresenterDependency,
        private val defaultLocationInteractor: DefaultLocationInteractor,
        private val commonLocationPermissionRequest: CommonLocationPermissionRequest
) : BaseSamplePresenter<DefaultLocationInteractorActivityView>(basePresenterDependency) {

    fun checkLocationAvailability() {
        view.showLoading()

        subscribeIo(
                defaultLocationInteractor.checkLocationAvailability(LocationPriority.BALANCED_POWER_ACCURACY),
                { hideLoadingAndShowLocationIsAvailable() },
                { t: Throwable -> hideLoadingAndShowLocationIsNotAvailable(t) }
        )
    }

    fun resolveLocationAvailability() {
        view.showLoading()

        val locationAvailabilityResolvingSingle =
                defaultLocationInteractor.checkLocationAvailability(LocationPriority.BALANCED_POWER_ACCURACY)
                        .toSingle { emptyList<Throwable>() }
                        .onErrorResumeNext { t: Throwable ->
                            if (t is CompositeException) {
                                defaultLocationInteractor.resolveLocationAvailability(t.exceptions)
                            } else {
                                Single.error(t)
                            }
                        }

        subscribeIo(
                locationAvailabilityResolvingSingle,
                { hideLoadingAndShowLocationIsAvailable() },
                { t: Throwable -> hideLoadingAndShowLocationIsNotAvailable(t) }
        )
    }

    fun getLastKnownLocation() {
        view.showLoading()

        val lastKnowLocationRequest = LastKnownLocationRequest(
                LocationPriority.HIGH_ACCURACY,
                true,
                commonLocationPermissionRequest
        )

        subscribeIo(
                defaultLocationInteractor.observeLastKnownLocationWithErrorsResolution(lastKnowLocationRequest),
                { location: Location -> hideLoadingAndShowLocation(location) },
                { hideLoadingAndShowNoLocation() },
                { t: Throwable -> hideLoadingAndShowLocationIsNotAvailable(t) }
        )
    }

    fun getCurrentLocation() {
        view.showLoading()

        val currentLocationRequest = CurrentLocationRequest(
                LocationPriority.HIGH_ACCURACY,
                10_000,
                0,
                true,
                commonLocationPermissionRequest
        )

        subscribeIo(
                defaultLocationInteractor.observeCurrentLocationWithErrorsResolution(currentLocationRequest),
                { location: Location -> hideLoadingAndShowLocation(location) },
                { t: Throwable -> hideLoadingAndShowLocationIsNotAvailable(t) }
        )
    }
}