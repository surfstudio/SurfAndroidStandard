package ru.surfstudio.android.core.navigation.sample.ui.screen.main

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.navigation.sample.R
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.navigation.sample.ui.screen.profile.ProfileRoute
import ru.surfstudio.android.core.ui.navigation.customtabs.CustomTabsNavigator
import ru.surfstudio.android.core.ui.navigation.customtabs.OpenUrlRoute
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.core.ui.provider.resource.ResourceProvider
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val activityNavigator: ActivityNavigator,
        private val customTabsNavigator: CustomTabsNavigator,
        private val resourceProvider: ResourceProvider
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val sm: MainScreenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(sm)
    }

    fun openProfileScreen() {
        activityNavigator.start(ProfileRoute(resourceProvider.getString(R.string.user_name)))
    }

    fun openLink(link: String) {
        customTabsNavigator.openLink(OpenUrlRoute(link))
    }
}