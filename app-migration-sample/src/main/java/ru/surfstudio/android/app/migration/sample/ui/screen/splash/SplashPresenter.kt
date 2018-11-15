package ru.surfstudio.android.app.migration.sample.ui.screen.splash

import ru.surfstudio.android.app.migration.sample.app.initialization.InitializeAppInteractor
import ru.surfstudio.android.app.migration.sample.ui.screen.main.MainActivityRoute
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import ru.surfstudio.android.dagger.scope.PerScreen
import java.util.concurrent.TimeUnit
import javax.inject.Inject

// время в миллисекундах, после которого переходит на другой экран
const val TRANSITION_DELAY_MS = 2000L

/**
 * Презентер загрузочного экрана
 */
@PerScreen
internal class SplashPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                                   private val activityNavigator: ActivityNavigator,
                                                   private val initializeAppInteractor: InitializeAppInteractor
) : BasePresenter<SplashActivityView>(basePresenterDependency) {

    private val sm = SplashScreenModel()

    // выбор роута для следующего экрана может зависеть от некоторой логики
    // например, в зависимости от того, авторизован ли пользователь
    private val nextRoute: ActivityRoute
        get() = MainActivityRoute()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(sm)
        if (!viewRecreated) {
            subscribeIoHandleError(
                    initializeAppInteractor
                            .initialize()
                            .delay(TRANSITION_DELAY_MS, TimeUnit.MILLISECONDS)) {
                activityNavigator.finishCurrent()
                openNextScreen()
            }
        }
    }

    private fun openNextScreen() {
        activityNavigator.start(nextRoute)
        activityNavigator.finishCurrent()
    }
}