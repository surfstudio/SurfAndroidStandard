package ru.surfstudio.standard.f_splash

import io.reactivex.Completable
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import ru.surfstudio.standard.base_ui.navigation.MainActivityRoute
import ru.surfstudio.standard.i_initialization.InitializeAppInteractor
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PerScreen
internal class SplashPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val activityNavigator: ActivityNavigator,
        private val route: SplashRoute,
        private val initializeAppInteractor: InitializeAppInteractor
) : BasePresenter<SplashActivityView>(basePresenterDependency) {

    private val nextRoute: ActivityRoute
        get() =
            MainActivityRoute()


    override fun onFirstLoad() {

        val delay = Completable.timer(TRANSITION_DELAY_MS, TimeUnit.MILLISECONDS)
        val worker = initializeAppInteractor.initialize()
        val merge = Completable
                .merge(arrayListOf(delay, worker))
                .toSingleDefault(EMPTY_STRING)

        subscribeIoHandleError(merge,
                {
                    activityNavigator.start(nextRoute)
                    activityNavigator.finishAffinity()
                },
                {
                    Logger.e(it)
                    activityNavigator.start(nextRoute)
                    activityNavigator.finishAffinity()
                })
    }

    companion object {
        /**
         * Минимальное время в миллисекундах в течении которого показывается сплэш
         */
        private val TRANSITION_DELAY_MS = 2000L
    }
}
