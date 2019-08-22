package ru.surfstudio.android.firebase.sample.ui.screen.main

import ru.surfstudio.android.analyticsv2.DefaultAnalyticService
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.firebase.sample.interactor.common.analytics.event.CustomEventV2
import ru.surfstudio.android.firebase.sample.interactor.common.analytics.event.SetUserProperty
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                                 private val analyticService: DefaultAnalyticService
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val sm: MainScreenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(sm)
    }

    fun sendEvent(stringValue: String) {
        analyticService.performAction(CustomEventV2(stringValue, 2, 3.0))
        analyticService.performAction(SetUserProperty("userKey", 2.0))
    }
}