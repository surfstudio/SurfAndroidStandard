package ru.surfstudio.android.app.migration.sample.ui.screen.splash

import io.reactivex.Completable
import ru.surfstudio.android.app.migration.sample.app.initialization.InitializeAppInteractor
import ru.surfstudio.android.app.migration.sample.ui.screen.main.MainActivityRoute
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import java.util.concurrent.TimeUnit
import javax.inject.Inject

// время в миллисекундах, после которого переходит на другой экран
const val TRANSITION_DELAY_MS = 2000L

/**
 * Презентер загрузочного экрана
 */
@PerScreen
internal class SplashPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val activityNavigator: ActivityNavigator,
        private val initializeAppInteractor: InitializeAppInteractor
) : BasePresenter<SplashActivityView>(basePresenterDependency) {

    private val sm = SplashScreenModel()

    // выбор роута для следующего экрана может зависеть от некоторой логики
    // например, в зависимости от того, авторизован ли пользователь
    private val nextRoute: ActivityRoute
        get() = MainActivityRoute()

    override fun onFirstLoad() {
        super.onFirstLoad()

        val delay = Completable.timer(TRANSITION_DELAY_MS, TimeUnit.MILLISECONDS)
        val worker = initializeAppInteractor.initialize()
        val merge = Completable
                .merge(arrayListOf(delay, worker))
                .toSingleDefault(EMPTY_STRING)

        subscribeIoHandleError(merge,
                {
                    openNextScreen()
                },
                {
                    Logger.e(it)
                    openNextScreen()
                })
    }

    private fun openNextScreen() {
        activityNavigator.start(nextRoute)
        activityNavigator.finishCurrent()
    }
}