package ru.surfstudio.android.security.sample.interactor.session

import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.security.session.SessionController
import javax.inject.Inject

private const val SESSION_DURATION = 10_000L

@PerApplication
class SessionInteractor @Inject constructor() : SessionController {

    override fun onSessionExpired() {
        Logger.d("Session has expired")
    }

    override fun getDurationMillis(): Long {
        return SESSION_DURATION
    }
}