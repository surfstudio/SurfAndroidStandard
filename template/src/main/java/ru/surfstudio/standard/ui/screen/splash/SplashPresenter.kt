package ru.surfstudio.standard.ui.screen.splash


import io.reactivex.Completable
import ru.surfstudio.android.core.app.dagger.scope.PerScreen
import ru.surfstudio.android.core.ui.base.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.base.navigation.activity.route.ActivityRoute
import ru.surfstudio.android.core.ui.base.screen.presenter.BasePresenter
import ru.surfstudio.android.core.ui.base.screen.presenter.BasePresenterDependency
import ru.surfstudio.standard.app.intialization.InitializeAppInteractor
import ru.surfstudio.standard.ui.screen.main.MainActivityRoute
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * Презентер для сплэш экрана.
 */
@PerScreen
internal class SplashPresenter @Inject
constructor(private val activityNavigator: ActivityNavigator,
            private val initializeAppInteractor: InitializeAppInteractor,
            basePresenterDependency: BasePresenterDependency,
            private val route: SplashRoute)
    : BasePresenter<SplashActivityView>(basePresenterDependency) {

    private val nextRoute: ActivityRoute
        get() {
            return MainActivityRoute()
        }

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        if (!viewRecreated) {
            val delay = Completable.timer(TRANSITION_DELAY_MS, TimeUnit.MILLISECONDS)
            val work = initializeAppInteractor.initialize()// полезная работа выполняется в этом Observable
            val merge = Completable.merge(arrayListOf(delay, work))
            subscribeIoHandleError(merge.toObservable<Unit>(),
                    { },
                    { activityNavigator.start(nextRoute) },
                    null)
        }
    }

    companion object {
        /**
         * Минимальное время в миллисекундах в течении которого показывается сплэш
         */
        private val TRANSITION_DELAY_MS = 2000L
    }
}
