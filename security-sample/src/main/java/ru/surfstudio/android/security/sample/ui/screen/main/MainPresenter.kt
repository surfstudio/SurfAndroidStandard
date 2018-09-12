package ru.surfstudio.android.security.sample.ui.screen.main

import androidx.core.widget.toast
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.security.auth.WrongEnterPinAttemptStorage
import ru.surfstudio.android.security.root.RootChecker
import ru.surfstudio.android.security.sample.ui.screen.session.SessionActivityRoute
import javax.inject.Inject

private const val CORRECT_PIN = "0000"

/**
 * Презентер главного экрана
 */
@PerScreen
class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                        private val activityNavigator: ActivityNavigator,
                                        private val wrongEnterPinAttemptStorage: WrongEnterPinAttemptStorage
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val screenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
    }

    fun checkRoot() {
        if (RootChecker.isRoot) {
            view.toast("Root detected!")
        } else {
            view.toast("No Root")
        }
    }

    fun openSession() {
        activityNavigator.start(SessionActivityRoute())
    }

    fun enterPin(pinCode: String) {
        //todo add ProfileInteractor
        //todo add string resources
        wrongEnterPinAttemptStorage.increaseAttemptCount()
        if (pinCode == CORRECT_PIN) {
            view.toast("Корректный pin-код")
            wrongEnterPinAttemptStorage.resetAttemptCount()
        } else {
            val attemptsCount = wrongEnterPinAttemptStorage.getAttemptCount()
            if (attemptsCount >= MainScreenModel.MAX_ENTER_PIN_ATTEMPT_COUNT) {
                view.toast("Количество неверный попыток ввода pin равно $attemptsCount")
            } else {
                view.toast("Неверный pin-код")
            }
        }
        view.render(screenModel)
    }
}
