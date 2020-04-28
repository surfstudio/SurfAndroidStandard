package ru.surfstudio.android.core.navigation.sample.ui.screen.profile

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class ProfilePresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        route: ProfileRoute
) : BasePresenter<ProfileActivityView>(basePresenterDependency) {

    private val sm: ProfileScreenModel = ProfileScreenModel(route.userName)

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(sm)
    }
}