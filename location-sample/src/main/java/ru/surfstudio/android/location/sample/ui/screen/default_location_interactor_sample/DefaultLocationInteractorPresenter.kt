package ru.surfstudio.android.location.sample.ui.screen.default_location_interactor_sample

import android.location.Location
import io.reactivex.*
import io.reactivex.exceptions.CompositeException
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.location.DefaultLocationInteractor
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

        subscribeIo(
                defaultLocationInteractor.checkLocationAvailability(),
                { hideLoadingAndShowLocationIsAvailable() },
                { t: Throwable -> hideLoadingAndShowLocationIsNotAvailable(t) }
        )
    }

    fun resolveLocationAvailability() {
        view.showLoading()

        val locationAvailabilityResolvingSingle =
                defaultLocationInteractor.checkLocationAvailability()
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

        subscribeIo(
                defaultLocationInteractor.observeLastKnownLocation(),
                { location: Location -> hideLoadingAndShowLocation(location) },
                { hideLoadingAndShowNoLocation() },
                { t: Throwable -> hideLoadingAndShowLocationIsNotAvailable(t) }
        )
    }

    fun getCurrentLocation() {
        view.showLoading()

        subscribeIo(
                defaultLocationInteractor.observeCurrentLocation(10_000),
                { location: Location -> hideLoadingAndShowLocation(location) },
                { t: Throwable -> hideLoadingAndShowLocationIsNotAvailable(t) }
        )
    }
}