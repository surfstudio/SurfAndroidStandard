package ru.surfstudio.android.core.ui.sample.ui.screen.result

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import javax.inject.Inject

class ResultPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val activityNavigator: ActivityNavigator,
        private val resultRoute: ResultActivityRoute
) : BasePresenter<ResultActivityView>(basePresenterDependency) {

    private val sm: ResultScreenModel = ResultScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        activityNavigator.finishWithResultNoData(true)
        view.render(sm)
    }
}