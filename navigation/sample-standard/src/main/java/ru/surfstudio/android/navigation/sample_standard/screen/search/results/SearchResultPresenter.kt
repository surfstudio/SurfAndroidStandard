package ru.surfstudio.android.navigation.sample_standard.screen.search.results

import android.util.Log
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.navigation.command.fragment.RemoveAll
import ru.surfstudio.android.navigation.command.fragment.Replace
import ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand.Companion.ACTIVITY_NAVIGATION_TAG
import ru.surfstudio.android.navigation.executor.NavigationCommandExecutor
import ru.surfstudio.android.navigation.sample_standard.screen.base.presenter.CommandExecutionPresenter
import ru.surfstudio.android.navigation.sample_standard.screen.search.request.SearchRequestRoute
import ru.surfstudio.android.navigation.sample_standard.utils.animations.ModalAnimations
import javax.inject.Inject

@PerScreen
class SearchResultPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        override val commandExecutor: NavigationCommandExecutor,
        private val bm: SearchResultBindModel,
        private val route: SearchResultRoute
) : BaseRxPresenter(basePresenterDependency), CommandExecutionPresenter {

    override fun onFirstLoad() {
        bm.backClick.bindTo {
            listOf(
                    RemoveAll(),
                    Replace(
                            route = SearchRequestRoute(bm.textState.value),
                            animations = ModalAnimations,
                            sourceTag = ACTIVITY_NAVIGATION_TAG
                    )
            ).execute()
        }
    }
}