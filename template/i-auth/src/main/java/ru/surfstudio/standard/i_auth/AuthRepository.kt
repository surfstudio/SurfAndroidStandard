package ru.surfstudio.standard.i_auth

import io.reactivex.Completable
import io.reactivex.Single
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.standard.i_network.network.transform
import ru.surfstudio.standard.domain.login.KeyInfo
import ru.surfstudio.standard.domain.login.LoginInfo
import ru.surfstudio.standard.i_auth.request.LoginByCodeRequest
import ru.surfstudio.standard.i_auth.request.LoginByPhoneRequest
import ru.surfstudio.standard.i_network.service.BaseNetworkService
import javax.inject.Inject

private const val CODE_FORMAT = "%s:%s"

/**
 * Сервис, отвечающий за авторизацию и регистрацию пользователя
 */
@PerApplication
class AuthRepository @Inject constructor(
        private val authApi: AuthApi
) : BaseNetworkService() {

    /**
     * Отсылка номера телефона для получения кода авторизации
     */
    fun requestCode(phoneNumber: String): Single<KeyInfo> =
            authApi.requestCode(LoginByPhoneRequest(phoneNumber))
                    .transform()

    /**
     * Вход по полученному из смс коду
     */
    fun loginByCode(key: String, smsCode: String): Single<LoginInfo> {
        val code = String.format(CODE_FORMAT, key, smsCode)
        return authApi.loginByCode(LoginByCodeRequest(code))
                .transform()
    }

    /**
     * Выход текущего авторизованного пользователя
     */
    fun logout(): Completable = authApi.logout()
}