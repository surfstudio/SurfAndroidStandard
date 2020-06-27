package ru.surfstudio.android.navigation.sample_standard.screen.bottom_navigation.home

import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.executor.NavigationCommandExecutor
import javax.inject.Inject

@PerScreen
class HomePresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val bm: HomeBindModel,
        private val commandExecutor: NavigationCommandExecutor
) : BaseRxPresenter(basePresenterDependency) {

    override fun onFirstLoad() {
        bm.openNestedScreenAction.bindTo { TODO() }
    }
}