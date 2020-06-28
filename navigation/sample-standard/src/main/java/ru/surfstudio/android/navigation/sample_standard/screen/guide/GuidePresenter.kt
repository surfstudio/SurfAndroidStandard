package ru.surfstudio.android.navigation.sample_standard.screen.guide

import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.command.fragment.Replace
import ru.surfstudio.android.navigation.executor.NavigationCommandExecutor
import ru.surfstudio.android.navigation.sample_standard.screen.base.presenter.CommandExecutionPresenter
import ru.surfstudio.android.navigation.sample_standard.screen.bottom_navigation.BottomNavRoute
import javax.inject.Inject

@PerScreen
class GuidePresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val bm: GuideBindModel,
        override val commandExecutor: NavigationCommandExecutor
) : BaseRxPresenter(basePresenterDependency), CommandExecutionPresenter {

    override fun onFirstLoad() {
        bm.bottomNavClicked.bindTo {
            Replace(BottomNavRoute()).execute()
        }
    }
}