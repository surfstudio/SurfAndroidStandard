package ru.surfstudio.android.core.mvp.binding

import ru.surfstudio.android.core.mvp.activity.CoreActivityView
import ru.surfstudio.android.core.mvp.model.ScreenModel
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency

abstract class BaseBindingPresenter<M: ScreenModel, V : BaseBindingView<M>>(
        basePresenterDependency: BasePresenterDependency)
    : BasePresenter<V>(basePresenterDependency) {

    abstract val screenModel: M

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.onBind(screenModel)
    }
}

abstract class BaseBindingView<in M: ScreenModel>: CoreActivityView() {

    abstract fun onBind(screenModel: M)
}