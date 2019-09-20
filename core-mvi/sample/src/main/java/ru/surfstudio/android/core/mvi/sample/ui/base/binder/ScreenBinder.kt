package ru.surfstudio.android.core.mvi.sample.ui.base.binder

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.ui.binder.RxBinder
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency

/**
 * Реализация сущность [RxBinder], используемая на экране приложения.
 *
 * Если необходима другая реализация - переопределить на проекте
 *
 * TODO убрать зависимость от presenter
 */
class ScreenBinder(
        basePresenterDependency: BasePresenterDependency
) : RxBinder, BaseRxPresenter(basePresenterDependency) {

    override fun <T> subscribe(observable: Observable<T>, onNext: (T) -> Unit, onError: (Throwable) -> Unit) =
            super.subscribe(observable, onNext, onError)
}