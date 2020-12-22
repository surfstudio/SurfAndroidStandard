package ru.rivegauche.app.f_main.bar

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.standard.f_main.bar.MainBarEvent
import ru.surfstudio.standard.f_main.bar.MainBarEvent.Navigation
import ru.surfstudio.standard.f_main.bar.MainBarEvent.TabSelected
import ru.surfstudio.standard.ui.mvi.navigation.AppNavigationMiddleware
import ru.surfstudio.standard.ui.mvi.navigation.extension.builder
import ru.surfstudio.standard.ui.mvi.navigation.extension.replace
import ru.surfstudio.standard.ui.navigation.routes.FeedFragmentRoute
import ru.surfstudio.standard.ui.navigation.routes.MainTabType.*
import ru.surfstudio.standard.ui.navigation.routes.ProfileFragmentRoute
import ru.surfstudio.standard.ui.navigation.routes.SearchFragmentRoute
import javax.inject.Inject

@PerScreen
internal class MainBarMiddleware @Inject constructor(
        basePresenterDependency: BaseMiddlewareDependency,
        private val sh: MainBarStateHolder,
        private val navigationMiddleware: AppNavigationMiddleware
) : BaseMiddleware<MainBarEvent>(basePresenterDependency) {

    override fun transform(eventStream: Observable<MainBarEvent>): Observable<out MainBarEvent> = transformations(eventStream) {
        addAll(
                Navigation::class decomposeTo navigationMiddleware,
                onCreate().map { TabSelected(sh.value.selectedTab) },
                TabSelected::class mapTo ::openSelectedTab
        )
    }

    private fun openSelectedTab(event: TabSelected): MainBarEvent {
        val tabRoute: FragmentRoute = when (event.tabType) {
            FEED -> FeedFragmentRoute()
            PROFILE -> ProfileFragmentRoute()
            SEARCH -> SearchFragmentRoute()
        }
        return Navigation().builder()
                .replace(tabRoute)
                .build()
    }
}
