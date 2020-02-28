package ru.surfstudio.android.security.sample.interactor.session

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.security.session.SessionActivityCallback
import ru.surfstudio.android.security.session.SessionManager

@Module
class SessionModule {

    @Provides
    @PerApplication
    fun provideSessionInteractor(): SessionInteractor {
        return SessionInteractor()
    }

    @Provides
    @PerApplication
    fun provideSessionManager(sessionInteractor: SessionInteractor): SessionManager {
        return SessionManager(sessionInteractor)
    }

    @Provides
    @PerApplication
    fun provideSessionActivtiyListener(sessionManager: SessionManager): SessionActivityCallback {
        return SessionActivityCallback(sessionManager)
    }
}