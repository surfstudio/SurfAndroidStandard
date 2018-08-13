package ru.surfstudio.standrad.i_auth

import ru.surfstudio.standard.domain.login.KeyInfo
import ru.surfstudio.standard.domain.login.LoginInfo
import ru.surfstudio.standrad.i_auth.request.LoginByCodeRequest
import ru.surfstudio.standrad.i_auth.request.LoginByPhoneRequest
import ru.surfstudio.standard.i_network.service.BaseNetworkService
import io.reactivex.Observable
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.network.TransformUtil
import javax.inject.Inject


private const val CODE_FORMAT = "%s:%s"

/**
 * Сервис, отвечающий за авторизацию и регистрацию пользователя
 */
@PerApplication
class AuthRepository @Inject
constructor(private val authApi: AuthApi) : BaseNetworkService() {

    /**
     * Отсылка номера телефона, для получения кода авторизации
     */
    fun requestCode(phoneNumber: String): Observable<KeyInfo> {
        return authApi.requestCode(LoginByPhoneRequest(phoneNumber))
                .map({ TransformUtil.transform(it) })
    }

    /**
     * Вход по полученному из смс коду
     */
    fun loginByCode(key: String, smsCode: String): Observable<LoginInfo> {
        val code = String.format(CODE_FORMAT, key, smsCode)
        return authApi.loginByCode(LoginByCodeRequest(code))
                .map({ TransformUtil.transform(it) })
    }

    /**
     * Выход текущего авторизованного пользователя
     */
    fun logout(): Observable<Unit> {
        return authApi.logout()
    }
}