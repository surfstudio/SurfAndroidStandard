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

        model.incAction.subscribe { _ -> model.counterState.getConsumer().accept(100) }
        model.decAction.subscribe { _ -> model.counterState.getConsumer().accept(20)  }

        model.doubleTextAction.subscribe { _ -> model.textEditState.a()}
    }
}