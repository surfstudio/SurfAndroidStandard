package ru.surfstudio.android.custom_scope_sample.ui.screen.another

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.custom_scope_sample.ui.base.LoginScopeStorage
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.login.LoginData
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
class AnotherPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val loginData: LoginData
) : BasePresenter<AnotherActivityView>(basePresenterDependency) {

    private val screenModel = AnotherScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)

        loginData.email = "test@test.com"
    }

}