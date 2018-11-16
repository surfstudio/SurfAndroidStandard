package ru.surfstudio.android.custom_scope_sample.ui.screen.main

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.custom_scope_sample.domain.EmailData
import ru.surfstudio.android.custom_scope_sample.ui.screen.another.AnotherActivityRoute
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                                 private val activityNavigator: ActivityNavigator,
                                                 private val emailData: EmailData
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val sm: MainScreenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(sm)
    }

    override fun onResume() {
        super.onResume()
        view.showMessage(emailData.email)
    }

    fun openAnotherScreen() {
        activityNavigator.start(AnotherActivityRoute())
    }
}