package ru.surfstudio.android.custom_scope_sample.ui.screen.main

import org.jetbrains.anko.toast
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.custom_scope_sample.ui.screen.another.AnotherActivityRoute
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.custom_scope_sample.ui.base.LoginScopeStorage
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.login.LoginData
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                                 private val activityNavigator: ActivityNavigator,
                                                 private val loginData: LoginData
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val screenModel: MainScreenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)

    }

    override fun onResume() {
        super.onResume()

        view.toast(loginData.email)
    }

    fun openAnotherScreen() = activityNavigator.start(AnotherActivityRoute())
}