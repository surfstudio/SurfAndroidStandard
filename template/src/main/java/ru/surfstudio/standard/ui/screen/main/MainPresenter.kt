package ru.surfstudio.standard.ui.screen.main

import ru.surfstudio.android.core.ui.base.screen.presenter.BasePresenter
import ru.surfstudio.android.core.ui.base.screen.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.interactor.analytics.AnalyticsService
import ru.surfstudio.standard.interactor.analytics.event.EnterEvent
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(
        private val analyticsService: AnalyticsService,
        basePresenterDependency: BasePresenterDependency
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val screenModel: MainScreenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)

        analyticsService.sendEvent(EnterEvent())
    }
}