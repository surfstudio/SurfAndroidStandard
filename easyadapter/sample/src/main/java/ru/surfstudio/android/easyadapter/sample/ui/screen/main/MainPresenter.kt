package ru.surfstudio.android.easyadapter.sample.ui.screen.main

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.easyadapter.sample.ui.screen.async.AsyncInflateListActivityRoute
import ru.surfstudio.android.easyadapter.sample.ui.screen.async_diff.AsyncDiffActivityRoute
import ru.surfstudio.android.easyadapter.sample.ui.screen.multitype.MultitypeListActivityRoute
import ru.surfstudio.android.easyadapter.sample.ui.screen.pagination.PaginationListActivityRoute
import javax.inject.Inject

@PerScreen
internal class MainPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val activityNavigator: ActivityNavigator
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val sm: MainScreenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(sm)
    }

    fun showMultitypeList() {
        activityNavigator.start(MultitypeListActivityRoute())
    }

    fun showPagintationList() {
        activityNavigator.start(PaginationListActivityRoute())
    }

    fun showAsyncInflateList() {
        activityNavigator.start(AsyncInflateListActivityRoute())
    }

    fun showAsyncDiffList() {
        activityNavigator.start(AsyncDiffActivityRoute())
    }
}