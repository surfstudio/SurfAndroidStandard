package ru.surfstudio.android.loadstate.sample.ui.screen.main

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.loadstate.sample.ui.screen.ordinary.DefaultRendererDemoActivityRoute
import ru.surfstudio.android.loadstate.sample.ui.screen.stubs.RendererWithStubsDemoActivityRoute
import javax.inject.Inject

/**
 * Презентер главного экрана семпла работы с лоадстейтами
 */
@PerScreen
class MainPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val activityNavigator: ActivityNavigator
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    fun openDefaultStatesScreen() {
        activityNavigator.start(DefaultRendererDemoActivityRoute())
    }

    fun openStubsStatesScreen() {
        activityNavigator.start(RendererWithStubsDemoActivityRoute())
    }
}
