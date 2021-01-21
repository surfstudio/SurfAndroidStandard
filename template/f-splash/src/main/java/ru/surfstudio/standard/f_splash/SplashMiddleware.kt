package ru.surfstudio.standard.f_splash

import io.reactivex.Completable
import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import ru.surfstudio.standard.f_splash.SplashEvent.Navigation
import ru.surfstudio.standard.i_initialization.InitializeAppInteractor
import ru.surfstudio.standard.ui.mvi.navigation.AppNavigationMiddleware
import ru.surfstudio.standard.ui.mvi.navigation.extension.replace
import ru.surfstudio.standard.ui.navigation.routes.MainActivityRoute
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
        private val navigationMiddleware: AppNavigationMiddleware,
        private val initializeAppInteractor: InitializeAppInteractor
) : BaseMiddleware<SplashEvent>(baseMiddlewareDependency) {

    override fun transform(eventStream: Observable<SplashEvent>) =
            transformations(eventStream) {
                addAll(
                        Navigation::class decomposeTo navigationMiddleware,
                        mergeInitDelay().map { openNextScreen() }
                )
            }

    private fun mergeInitDelay(): Observable<String> {
        val delay = Completable.timer(TRANSITION_DELAY_MS, TimeUnit.MILLISECONDS)
        val worker = initializeAppInteractor.initialize()
        return Completable.merge(arrayListOf(delay, worker))
                .io()
                .toSingleDefault(EMPTY_STRING)
                .toObservable()
    }

    private fun openNextScreen(): SplashEvent {
        val nextRoute = MainActivityRoute()
        return Navigation().replace(nextRoute)
    }
}