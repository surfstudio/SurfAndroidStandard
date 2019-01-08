package ru.surfstudio.core_mvp_rxbinding_sample

import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.core_mvp_rxbinding.base.ui.BaseRxPresenter
import javax.inject.Inject

class MainPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency
): BaseRxPresenter<MainModel, MainActivityView>(basePresenterDependency) {
    private val model = MainModel()
    override fun getRxModel() = model

    override fun onFirstLoad() {
        super.onFirstLoad()

        model.incAction.subscribe { model.textViewStatus.accept(model.textViewStatus.value + 1) }
        model.decAction.subscribe { model.textViewStatus.accept(model.textViewStatus.value - 1) }
    }
}