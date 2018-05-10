package ru.surfstudio.android.core.mvp.binding

import ru.surfstudio.android.core.mvp.model.ScreenModel
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.mvp.view.CoreView

/**
 * Вспомогательный презентер для работы с [BindData]
 */
abstract class BaseBindingPresenter<M : ScreenModel, V>(basePresenterDependency: BasePresenterDependency)
    : BasePresenter<V>(basePresenterDependency)
        where  V : CoreView, V : BindableView<M> {

    abstract val screenModel: M

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.onBind(screenModel)
    }
}