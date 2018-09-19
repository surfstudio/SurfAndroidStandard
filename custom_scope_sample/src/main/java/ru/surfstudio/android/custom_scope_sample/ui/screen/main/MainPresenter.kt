package ru.surfstudio.android.custom_scope_sample.ui.screen.main

import org.jetbrains.anko.toast
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.custom_scope_sample.ui.screen.another.AnotherActivityRoute
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.custom_scope_sample.domain.EmailData
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

    private val screenModel: MainScreenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)

    }

    override fun onResume() {
        super.onResume()
        view.toast(emailData.email)
    }

    fun openAnotherScreen() {
        activityNavigator.start(AnotherActivityRoute())
    }
}