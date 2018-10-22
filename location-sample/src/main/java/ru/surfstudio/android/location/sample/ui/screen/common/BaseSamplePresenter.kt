package ru.surfstudio.android.location.sample.ui.screen.common

import android.location.Location
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency

/**
 * Базовый презентер для примеров использования модуля местоположения
 */
open class BaseSamplePresenter<V : BaseSampleActivity>(
        basePresenterDependency: BasePresenterDependency
) : BasePresenter<V>(basePresenterDependency) {

    protected fun hideLoadingAndShowLocation(location: Location) {
        view.hideLoading()
        view.showLocation(location)
    }

    protected fun hideLoadingAndShowNoLocation() {
        view.hideLoading()
        view.showNoLocation()
    }

    protected fun hideLoadingAndShowLocationIsAvailable() {
        view.hideLoading()
        view.showLocationIsAvailable()
    }

    protected fun hideLoadingAndShowLocationIsNotAvailable(t: Throwable) {
        view.hideLoading()
        view.showLocationIsNotAvailable(t)
    }
}