package ru.surfstudio.standard.interactor.auth

import ru.surfstudio.android.core.app.dagger.scope.PerApplication
import ru.surfstudio.standard.domain.phone.LoginInfo
import rx.Observable
import rx.subjects.PublishSubject
import javax.inject.Inject

/**
 * инкапсулирует действия, которые необходимо выполнить при смене сессии/пользователя
 */
@PerApplication
class SessionChangedInteractor @Inject
constructor(private val tokenStorage: TokenStorage) {
    private val sessionChangedPublishSubject = PublishSubject.create<LoginState>()

    fun observeSessionChanged(): Observable<LoginState> {
        return sessionChangedPublishSubject
    }

    fun onLogin(authInfo: LoginInfo) {
        tokenStorage.saveTokens(authInfo.accessToken!!, authInfo.refreshToken!!)
        sessionChangedPublishSubject.onNext(LoginState.LOGGED_IN)
    }

    fun onForceLogout() {
        tokenStorage.clearTokens()
        sessionChangedPublishSubject.onNext(LoginState.NOT_AUTHORIZED)
    }
}