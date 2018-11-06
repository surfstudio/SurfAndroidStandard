package ru.surfstudio.standard.i_session

import ru.surfstudio.standard.domain.login.LoginInfo
import ru.surfstudio.standard.i_token.TokenStorage
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import ru.surfstudio.android.dagger.scope.PerApplication
import javax.inject.Inject

/**
 * инкапсулирует действия, которые необходимо выполнить при смене сессии/пользователя
 */
@PerApplication
class SessionChangedInteractor @Inject constructor(private val tokenStorage: TokenStorage) {

    private val sessionChangedPublishSubject = PublishSubject.create<LoginState>()

    fun observeSessionChanged(): Observable<LoginState> {
        return sessionChangedPublishSubject
    }

    fun onLogin(loginInfo: LoginInfo) {
        tokenStorage.token = loginInfo.accessToken
        sessionChangedPublishSubject.onNext(LoginState.LOGGED_IN)
    }

    fun onForceLogout() {
        clearStorage()
        sessionChangedPublishSubject.onNext(LoginState.NOT_AUTHORIZED)
    }

    private fun clearStorage() {
        tokenStorage.clearTokens()
    }
}
