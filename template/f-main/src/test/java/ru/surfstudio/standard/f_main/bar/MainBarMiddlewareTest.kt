package ru.surfstudio.standard.f_main.bar

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.should
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.types.shouldBeSameInstanceAs
import ru.surfstudio.android.navigation.command.fragment.Replace
import ru.surfstudio.android.navigation.route.tab.TabHeadRoute
import ru.surfstudio.android.rx.extension.toObservable
import ru.surfstudio.standard.base.test.util.forAll
import ru.surfstudio.standard.f_main.bar.MainBarEvent.TabSelected
import ru.surfstudio.standard.ui.navigation.routes.FeedFragmentRoute
import ru.surfstudio.standard.ui.navigation.routes.MainTabType
import ru.surfstudio.standard.ui.navigation.routes.MainTabType.FEED
import ru.surfstudio.standard.ui.navigation.routes.MainTabType.PROFILE
import ru.surfstudio.standard.ui.navigation.routes.MainTabType.SEARCH
import ru.surfstudio.standard.ui.navigation.routes.ProfileFragmentRoute
import ru.surfstudio.standard.ui.navigation.routes.SearchFragmentRoute
import ru.surfstudio.standard.ui.test.base.BaseMiddlewareTest
import ru.surfstudio.standard.ui.test.matcher.shouldBeNavigationCommand
import kotlin.reflect.KClass

internal class MainBarMiddlewareTest : BaseMiddlewareTest() {

    private val sh = MainBarStateHolder()

    @Test
    fun `when tab of given type selected, corresponding tab screen should be opened`() = forAll(
        FEED to FeedFragmentRoute::class,
        PROFILE to ProfileFragmentRoute::class,
        SEARCH to SearchFragmentRoute::class
    ) { tabType: MainTabType, routeClass: KClass<out TabHeadRoute> ->
        val middleware = createMiddleware()
        val inputEvent = TabSelected(tabType)

        val testObserver = middleware.transform(inputEvent.toObservable()).test()

        assertSoftly(testObserver.values().firstOrNull()) {
            shouldBeInstanceOf<MainBarEvent.Navigation>()
                .event
                .shouldBeNavigationCommand<Replace>()
                .route
                .should { route -> route::class.shouldBeSameInstanceAs(routeClass) }
        }
    }

    private fun createMiddleware(): MainBarMiddleware {
        return MainBarMiddleware(baseMiddlewareDependency, sh, navigationMiddleware)
    }
}