package ru.surfstudio.android.easyadapter.sample.ui.screen.multitype

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
internal class MultitypeListPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency
) : BasePresenter<MultitypeListActivityView>(basePresenterDependency) {

    private val sm: MultitypeListScreenModel = MultitypeListScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(sm)
    }
}