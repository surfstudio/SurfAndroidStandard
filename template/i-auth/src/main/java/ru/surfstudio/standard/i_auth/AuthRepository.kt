package ru.surfstudio.standard.i_auth

import io.reactivex.Observable
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.logger.RemoteLogger
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import ru.surfstudio.standard.domain.login.KeyInfo
import ru.surfstudio.standard.domain.login.LoginInfo
import ru.surfstudio.standard.i_network.service.BaseNetworkService
import ru.surfstudio.standard.i_auth.request.LoginByCodeRequest
import ru.surfstudio.standard.i_auth.request.LoginByPhoneRequest
import javax.inject.Inject

private const val CODE_FORMAT = "%s:%s"

/**
 * Сервис, отвечающий за авторизацию и регистрацию пользователя
 */
@PerApplication
class AuthRepository @Inject constructor(private val authApi: AuthApi) : BaseNetworkService() {

    /**
     * Отсылка номера телефона, для получения кода авторизации
     */
    fun requestCode(phoneNumber: String): Observable<KeyInfo> =
            authApi.requestCode(LoginByPhoneRequest(phoneNumber))
                    .map { it.transform() }

    /**
     * Вход по полученному из смс коду
     */
    fun loginByCode(key: String, smsCode: String): Observable<LoginInfo> {
        val code = String.format(CODE_FORMAT, key, smsCode)
        return authApi.loginByCode(LoginByCodeRequest(code))
                .map { it.transform() }
                .doOnNext { RemoteLogger.setUser(it.userId.toString(), EMPTY_STRING, EMPTY_STRING) } //todo настроить в приложении
    }

    /**
     * Выход текущего авторизованного пользователя
     */
    fun logout(): Observable<Unit> =
            authApi.logout()
                    .doOnNext { RemoteLogger.clearUser() }
}