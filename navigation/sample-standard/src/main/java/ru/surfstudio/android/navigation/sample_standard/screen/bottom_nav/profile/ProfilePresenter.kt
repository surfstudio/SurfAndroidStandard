package ru.surfstudio.android.navigation.sample_standard.screen.bottom_nav.profile

import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.command.activity.Start
import ru.surfstudio.android.navigation.command.dialog.Show
import ru.surfstudio.android.navigation.command.fragment.RemoveLast
import ru.surfstudio.android.navigation.command.fragment.Replace
import ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand.Companion.ACTIVITY_NAVIGATION_TAG
import ru.surfstudio.android.navigation.executor.NavigationCommandExecutor
import ru.surfstudio.android.navigation.observer.ScreenResultObserver
import ru.surfstudio.android.navigation.rx.extension.observeScreenResult
import ru.surfstudio.android.navigation.sample_standard.screen.base.dialog.simple.SimpleDialogResult
import ru.surfstudio.android.navigation.sample_standard.screen.base.presenter.CommandExecutionPresenter
import ru.surfstudio.android.navigation.sample_standard.screen.bottom_nav.profile.logout.LogoutConfirmationRoute
import ru.surfstudio.android.navigation.sample_standard.screen.bottom_nav.profile.settings.ApplicationSettingsRoute
import ru.surfstudio.android.navigation.sample_standard.screen.search.request.SearchRequestRoute
import ru.surfstudio.android.navigation.sample_standard.utils.animations.ModalAnimations
import javax.inject.Inject

@PerScreen
class ProfilePresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val bm: ProfileBindModel,
        private val screenResultObserver: ScreenResultObserver,
        override val commandExecutor: NavigationCommandExecutor
) : BaseRxPresenter(basePresenterDependency), CommandExecutionPresenter {

    override fun onFirstLoad() {
        val confirmLogoutRoute = LogoutConfirmationRoute()
        subscribeToLogoutResult(confirmLogoutRoute)
        bm.openConfirmLogoutScreen bindTo { Show(confirmLogoutRoute).execute() }
        bm.openSettings.bindTo { Start(ApplicationSettingsRoute()).execute() }
        bm.openSearch.bindTo {
            Replace(
                    route = SearchRequestRoute(),
                    animations = ModalAnimations,
                    sourceTag = ACTIVITY_NAVIGATION_TAG
            ).execute()
        }
    }

    private fun subscribeToLogoutResult(confirmLogoutRoute: LogoutConfirmationRoute) {
        subscribe(screenResultObserver.observeScreenResult(confirmLogoutRoute)) { result ->
            if (result == SimpleDialogResult.POSITIVE) {
                RemoveLast(sourceTag = ACTIVITY_NAVIGATION_TAG).execute()
            }
        }
    }
}