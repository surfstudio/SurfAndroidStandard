package ru.surfstudio.android.location.sample.ui.screen.base

import android.location.Location
import io.reactivex.*
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider

/**
 * Базовый презентер для примеров использования модуля местоположения
 */
open class BaseSamplePresenter<V : BaseSampleActivity>(
        screenEventDelegateManager: ScreenEventDelegateManager,
        screenState: ScreenState,
        private val schedulersProvider: SchedulersProvider
) : CorePresenter<V>(screenEventDelegateManager, screenState) {

    protected fun <T> Observable<T>.configureAndSubscribe(observer: Observer<T>) {
        subscribeOn(schedulersProvider.worker()).observeOn(schedulersProvider.main()).subscribe(observer)
    }

    protected fun <T> Single<T>.configureAndSubscribe(singleObserver: SingleObserver<T>) {
        subscribeOn(schedulersProvider.worker()).observeOn(schedulersProvider.main()).subscribe(singleObserver)
    }

    protected fun <T> Maybe<T>.configureAndSubscribe(maybeObserver: MaybeObserver<T>) {
        subscribeOn(schedulersProvider.worker()).observeOn(schedulersProvider.main()).subscribe(maybeObserver)
    }

    protected fun Completable.configureAndSubscribe(completableObserver: CompletableObserver) {
        subscribeOn(schedulersProvider.worker()).observeOn(schedulersProvider.main()).subscribe(completableObserver)
    }

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