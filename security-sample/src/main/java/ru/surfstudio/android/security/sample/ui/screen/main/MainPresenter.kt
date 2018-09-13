package ru.surfstudio.android.security.sample.ui.screen.main

import androidx.core.widget.toast
import ru.surfstudio.android.core.app.StringsProvider
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.security.auth.WrongEnterAttemptStorage
import ru.surfstudio.android.security.root.RootChecker
import ru.surfstudio.android.security.sample.R
import ru.surfstudio.android.security.sample.interactor.profile.ProfileInteractor
import ru.surfstudio.android.security.sample.ui.screen.session.SessionActivityRoute
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                        private val activityNavigator: ActivityNavigator,
                                        private val profileInteractor: ProfileInteractor,
                                        private val stringsProvider: StringsProvider,
                                        private val wrongEnterAttemptStorage: WrongEnterAttemptStorage
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val screenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
    }

    fun checkRoot() {
        view.toast(if (RootChecker.isRoot) R.string.root_message else R.string.no_root_message)
    }

    fun openSession() {
        activityNavigator.start(SessionActivityRoute())
    }

    fun enterPin(pinCode: String) {
        subscribeIoHandleError(profileInteractor.login(pinCode)) { isValidPin ->
            wrongEnterAttemptStorage.increasePinAttemptCount()
            if (isValidPin) {
                view.toast(R.string.valid_pin_message)
                wrongEnterAttemptStorage.resetAttemptsCount()
            } else {
                val attemptsCount = wrongEnterAttemptStorage.getPinAttemptCount()
                if (attemptsCount >= MainScreenModel.MAX_ENTER_PIN_ATTEMPT_COUNT) {
                    view.toast(stringsProvider.getString(R.string.pin_attempts_message, attemptsCount))
                } else {
                    view.toast(R.string.invalid_pin_message)
                }
            }
            view.render(screenModel)
        }
    }
}
