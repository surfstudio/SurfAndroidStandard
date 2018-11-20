package ru.surfstudio.android.mvpwidget.sample.ui.screen.main

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvpwidget.sample.interactor.ui.screen.list.ListActivityRoute
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val activityNavigator: ActivityNavigator
) : BasePresenter<MainActivityView>(basePresenterDependency) {
    fun openListScreen() {
        activityNavigator.start(ListActivityRoute())
    }
}