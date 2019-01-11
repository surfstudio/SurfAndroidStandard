package ru.surfstudio.android.core.mvp.rx.sample

import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.mvp.rx.ui.BaseRxPresenter
import javax.inject.Inject

class MainPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency
) : BaseRxPresenter<MainModel, MainActivityView>(basePresenterDependency) {
    private val model = MainModel()
    override fun getRxModel() = model

    override fun onFirstLoad() {
        super.onFirstLoad()

        subscribe(model.incAction.getObservable()) { _ -> model.counterState.getConsumer().accept(100) }
        subscribe(model.decAction.getObservable()) { _ -> model.counterState.getConsumer().accept(20) }
        subscribe(model.doubleTextAction.getObservable()) { _ -> model.textEditState.getConsumer().accept("123123") }
    }
}