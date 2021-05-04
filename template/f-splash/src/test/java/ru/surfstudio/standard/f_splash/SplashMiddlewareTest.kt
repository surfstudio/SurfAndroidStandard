package ru.surfstudio.standard.f_splash

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import ru.surfstudio.android.navigation.command.activity.Replace
import ru.surfstudio.standard.i_initialization.InitializeAppInteractor
import ru.surfstudio.standard.i_onboarding.OnBoardingStorage
import ru.surfstudio.standard.ui.navigation.routes.MainActivityRoute
import ru.surfstudio.standard.ui.test.base.BaseMiddlewareTest
import ru.surfstudio.standard.ui.test.matcher.shouldBeNavigationCommand
import ru.surfstudio.standard.ui.test.matcher.withRoute
import java.util.concurrent.TimeUnit.MILLISECONDS

internal class SplashMiddlewareTest : BaseMiddlewareTest() {

    private val initializeAppInteractor: InitializeAppInteractor = mockk {
        every { initialize() } returns Completable.complete()
    }

    private val onboardingStorage: OnBoardingStorage = mockk {
        every { shouldShowOnBoardingScreen } returns false
    }

    @After
    fun tearDownTest() {
        RxJavaPlugins.reset()
    }

    @Test
    fun `when delay elapsed, main screen should be opened`() {
        val testScheduler = TestScheduler()
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }
        val middleware = createMiddleware()

        val testObserver = middleware.transform(Observable.empty()).test()
        testScheduler.advanceTimeBy(TRANSITION_DELAY_MS, MILLISECONDS)

        assertSoftly(testObserver.values().firstOrNull()) {
            shouldBeInstanceOf<SplashEvent.Navigation>()
                    .event
                    .shouldBeNavigationCommand<Replace>()
                    .withRoute<MainActivityRoute>()
        }
    }

    private fun createMiddleware(): SplashMiddleware {
        return SplashMiddleware(
                baseMiddlewareDependency,
                navigationMiddleware,
                initializeAppInteractor,
                onboardingStorage
        )
    }
}