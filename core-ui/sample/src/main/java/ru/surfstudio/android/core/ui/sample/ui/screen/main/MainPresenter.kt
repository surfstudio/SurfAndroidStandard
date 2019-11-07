package ru.surfstudio.android.core.ui.sample.ui.screen.main

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.sample.ui.screen.result.ResultNoDataActivityRoute
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val activityNavigator: ActivityNavigator
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val sm: MainScreenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        subscribe(activityNavigator.observeResultNoData(ResultNoDataActivityRoute::class.java)) {
            view.showToast("Result from ResultNoDataActivityRoute is: " + it.isSuccess)
        }
        view.render(sm)
    }

    fun startResultActivity() {
        activityNavigator.startForResult(ResultNoDataActivityRoute())
    }
}