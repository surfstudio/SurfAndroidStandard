package ru.surfstudio.android.easyadapter.sample.ui.screen.pagination

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import javax.inject.Inject

class PaginationListPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency
) : BasePresenter<PaginationListActivityView>(basePresenterDependency) {

    private val screenModel: PaginationListScreenModel = PaginationListScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
    }
}