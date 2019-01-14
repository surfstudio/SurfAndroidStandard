package ru.surfstudio.android.core.mvp.rx.sample

import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.mvp.rx.sample.checkbox.CheckboxActivityRoute
import ru.surfstudio.android.core.mvp.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import javax.inject.Inject

class MainPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        val activityNavigator: ActivityNavigator
) : BaseRxPresenter<MainModel, MainActivityView>(basePresenterDependency) {
    private val model = MainModel()
    override fun getRxModel() = model

    override fun onFirstLoad() {
        super.onFirstLoad()

        subscribe(model.incAction.getObservable()) { _ -> model.counterState.getConsumer().accept(100) }
        subscribe(model.decAction.getObservable()) { _ -> model.counterState.getConsumer().accept(20) }
        subscribe(model.textEditState.getObservable()){ model.sampleCommand.getConsumer().accept(it)}
        subscribe(model.doubleTextAction.getObservable()) { _ -> model.textEditState.apply { getConsumer().accept(value + value) }}

        subscribe(model.checkboxActivityOpen.getObservable()) { _ -> activityNavigator.start(CheckboxActivityRoute()) }
    }
}