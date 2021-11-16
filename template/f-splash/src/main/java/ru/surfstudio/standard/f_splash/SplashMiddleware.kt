package ru.surfstudio.standard.f_splash

import android.os.Build
import io.reactivex.Completable
import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand
import ru.surfstudio.android.navigation.route.activity.ActivityRoute
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import ru.surfstudio.standard.f_splash.SplashEvent.Navigation
import ru.surfstudio.standard.i_initialization.InitializeAppInteractor
import ru.surfstudio.standard.i_onboarding.OnBoardingStorage
import ru.surfstudio.standard.ui.mvi.navigation.base.NavigationMiddleware
import ru.surfstudio.standard.ui.mvi.navigation.extension.*
import ru.surfstudio.standard.ui.navigation.routes.MainActivityRoute
import ru.surfstudio.standard.ui.navigation.routes.OnboardingFragmentRoute
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Минимальное время в миллисекундах, в течение которого показывается сплэш
 */
const val TRANSITION_DELAY_MS = 2000L

/**
 * Middleware сплэш экрана [SplashActivityView].
 */
@PerScreen
class SplashMiddleware @Inject constructor(
        baseMiddlewareDependency: BaseMiddlewareDependency,
        private val navigationMiddleware: NavigationMiddleware,
        private val initializeAppInteractor: InitializeAppInteractor,
        private val onBoardingStorage: OnBoardingStorage
) : BaseMiddleware<SplashEvent>(baseMiddlewareDependency) {

    override fun transform(eventStream: Observable<SplashEvent>) =
            transformations(eventStream) {
                addAll(
                        Navigation::class decomposeTo navigationMiddleware,
                        mergeInitDelay().map { openNextScreen() }
                )
            }

    private fun mergeInitDelay(): Observable<String> {
        val transitionDelay = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            TRANSITION_DELAY_MS / 4
        } else {
            TRANSITION_DELAY_MS
        }
        val delay = Completable.timer(transitionDelay, TimeUnit.MILLISECONDS)
        val worker = initializeAppInteractor.initialize()
        return Completable.merge(arrayListOf(delay, worker))
                .io()
                .toSingleDefault(EMPTY_STRING)
                .toObservable()
    }

    private fun openNextScreen(): SplashEvent {
        return if (onBoardingStorage.shouldShowOnBoardingScreen) {
            Navigation().replace(
                    OnboardingFragmentRoute(),
                    sourceTag = FragmentNavigationCommand.ACTIVITY_NAVIGATION_TAG,
            )
        } else {
            Navigation().replace(MainActivityRoute())
        }
    }
}