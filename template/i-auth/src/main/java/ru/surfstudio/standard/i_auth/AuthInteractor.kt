package ru.surfstudio.standard.i_auth

import android.annotation.SuppressLint
import io.reactivex.Completable
import io.reactivex.Single
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.standard.i_network.network.BaseNetworkInteractor
import ru.surfstudio.standard.domain.login.KeyInfo
import ru.surfstudio.standard.domain.login.LoginInfo
import ru.surfstudio.standard.f_debug.DebugInteractor
import ru.surfstudio.standard.i_session.SessionChangedInteractor
import javax.inject.Inject

/**
 * Интерактор, отвечающий за авторизацию пользователя
 */
@PerApplication
@SuppressLint("CheckResult")
class AuthInteractor @Inject constructor(
        connectionQualityProvider: ConnectionProvider,
        debugInteractor: DebugInteractor,
        private val authRepository: AuthRepository,
        private val sessionChangedInteractor: SessionChangedInteractor
) : BaseNetworkInteractor(connectionQualityProvider) {

    init {
        debugInteractor.observeNeedClearSession().subscribe {
            sessionChangedInteractor.onForceLogout()
        }
    }

    /**
     * Отсылка номера телефона для получения кода авторизации
     */
    fun requestCode(phoneNumber: String): Single<KeyInfo> =
            authRepository.requestCode(phoneNumber)

    /**
     * Вход по полученному из смс коду
     */
    fun loginByCode(key: String, smsCode: String): Single<LoginInfo> =
            authRepository.loginByCode(key, smsCode)
                    .doOnSuccess {
                        sessionChangedInteractor.onLogin(it)
                    }

    /**
     * Выход текущего авторизованного пользователя
     */
    fun logout(): Completable =
            authRepository.logout()
                    .doOnComplete {
                        sessionChangedInteractor.onForceLogout()
                    }
}