package ru.surfstudio.android.easyadapter.sample.ui.screen.multitype

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import javax.inject.Inject

class MultitypeListPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency
) : BasePresenter<MultitypeListActivityView>(basePresenterDependency) {

    private val screenModel: MultitypeListScreenModel = MultitypeListScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
    }
}