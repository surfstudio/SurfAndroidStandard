package ru.surfstudio.standard.interactor.auth

import ru.surfstudio.android.core.app.dagger.scope.PerApplication
import ru.surfstudio.android.network.TransformUtil
import ru.surfstudio.standard.domain.auth.phone.KeyInfo
import ru.surfstudio.standard.domain.auth.phone.LoginInfo
import ru.surfstudio.standard.domain.auth.recover.RecoverByEmailStatus
import ru.surfstudio.standard.domain.auth.recover.RecoverByPhoneStatus
import ru.surfstudio.standard.interactor.auth.network.AuthApi
import ru.surfstudio.standard.interactor.auth.network.request.*
import ru.surfstudio.standard.interactor.auth.network.service.BaseNetworkService
import rx.Observable
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
     * Вход по паре email и пароль
     */
    fun loginByEmail(email: String, password: String): Observable<LoginInfo> {
        return authApi.loginByEmail(LoginByEmailRequest(email, password))
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

    /**
     * Восстановление доступа через телефон
     */
    fun recoverByPhone(phone: String): Observable<RecoverByPhoneStatus> {
        return authApi.recoverByPhone(RecoverByPhoneRequest(phone))
                .map { TransformUtil.transform(it) as RecoverByPhoneStatus }
    }

    /**
     * Восстановление доступа через почту
     */
    fun recoverByEmail(email: String): Observable<RecoverByEmailStatus> {
        return authApi.recoverByEmail(RecoverByEmailRequest(email))
                .map { TransformUtil.transform(it) }
    }

}