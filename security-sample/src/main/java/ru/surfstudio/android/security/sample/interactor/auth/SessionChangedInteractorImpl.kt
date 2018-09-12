package ru.surfstudio.android.security.sample.interactor.auth

import ru.surfstudio.android.core.ui.navigation.activity.navigator.GlobalNavigator
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.security.sample.ui.screen.main.MainActivityRoute
import ru.surfstudio.android.security.session.SessionChangedInteractor

class SessionChangedInteractorImpl(private val globalNavigator: GlobalNavigator
) : SessionChangedInteractor {

    override fun onSessionInvalid() {
        Logger.d("Session changed")
        openAuthScreen()
    }

    fun openAuthScreen() {
        globalNavigator.start(MainActivityRoute(true))
    }
}