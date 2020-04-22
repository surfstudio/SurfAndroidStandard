package ru.surfstudio.android.navigation.sample_standard.screen.guide

import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.executor.NavigationCommandExecutor
import javax.inject.Inject

@PerScreen
class GuidePresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency
) : BaseRxPresenter(basePresenterDependency) {

    override fun onFirstLoad() {

    }
}