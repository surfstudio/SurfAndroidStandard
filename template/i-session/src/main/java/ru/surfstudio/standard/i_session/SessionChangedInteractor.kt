package ru.surfstudio.standard.i_session

import ru.surfstudio.standard.domain.entity.LoginInfoEntity
import ru.surfstudio.standard.i_token.TokenStorage
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.standard.base.logger.RemoteLogger
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import javax.inject.Inject

/**
 * инкапсулирует действия, которые необходимо выполнить при смене сессии/пользователя
 */
@PerApplication
class SessionChangedInteractor @Inject constructor(
        private val tokenStorage: TokenStorage
) {

    private val sessionChangedPublishSubject = PublishSubject.create<LoginState>()

    fun observeSessionChanged(): Observable<LoginState> {
        return sessionChangedPublishSubject
    }

    fun onLogin(loginInfo: LoginInfoEntity, clearStorages: Boolean = true) {
        if (clearStorages) {
            clearStorage()
        }
        with(loginInfo) {
            tokenStorage.token = accessToken
            RemoteLogger.setUser(userId.toString(), EMPTY_STRING, EMPTY_STRING) //todo настроить в приложении
        }
        sessionChangedPublishSubject.onNext(LoginState.LOGGED_IN)
    }

    fun onForceLogout() {
        clearStorage()
        RemoteLogger.clearUser()
        sessionChangedPublishSubject.onNext(LoginState.NOT_AUTHORIZED)
    }

    private fun clearStorage() {
        //todo очистить кэш и все хранилища при смене сессии
        tokenStorage.clearTokens()
    }
}
