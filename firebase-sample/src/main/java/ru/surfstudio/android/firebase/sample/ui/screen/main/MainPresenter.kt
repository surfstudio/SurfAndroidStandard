package ru.surfstudio.android.firebase.sample.ui.screen.main

import ru.surfstudio.android.analyticsv2.DefaultAnalyticService
import ru.surfstudio.android.firebase.sample.interactor.common.analytics.AnalyticsService
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.firebase.sample.interactor.common.analytics.event.CustomEventV2
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                                 private val analyticsService: AnalyticsService,
                                                 private val defaultAnalyticService: DefaultAnalyticService
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val sm: MainScreenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(sm)
    }

    fun sendEvent(stringValue: String) {
        analyticsService.trackEvent(stringValue)
        defaultAnalyticService.performAction(CustomEventV2("value1", 2, 3.0))
    }
}