package ru.surfstudio.android.security.sample.ui.screen.main

import androidx.core.widget.toast
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.security.root.RootChecker
import ru.surfstudio.android.security.sample.R
import ru.surfstudio.android.security.sample.ui.screen.session.SessionActivityRoute
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                        private val activityNavigator: ActivityNavigator
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val screenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
    }

    fun checkRoot() {
        view.toast(if (RootChecker.isRoot) R.string.root_message else R.string.no_root_message)
    }

    fun openSession() {
        activityNavigator.start(SessionActivityRoute())
    }
}
