package ru.surfstudio.standard.interactor.auth

import com.google.common.collect.ImmutableMap
import ru.surfstudio.android.core.app.dagger.scope.PerApplication
import ru.surfstudio.android.core.app.interactor.common.network.TransformUtil
import ru.surfstudio.standard.domain.phone.KeyInfo
import ru.surfstudio.standard.domain.phone.LoginInfo
import ru.surfstudio.standard.interactor.auth.network.*
import ru.surfstudio.standard.interactor.auth.network.request.LoginByPhoneRequest
import ru.surfstudio.standard.interactor.auth.network.service.BaseNetworkService
import rx.Observable
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE
import javax.inject.Inject

const val IS_REFRESHING_TOKEN_KEY = "is_refreshing_key" //используется для опеределения поптыки обновления токена
private const val GRAND_TYPE_KEY = "grant_type"
private const val CLIENT_ID_KEY = "client_id"
private const val REFRESH_TOKEN_KEY = "refresh_token"
private const val CODE_KEY = "code"
private const val REDIRECT_URI_KEY = "redirect_uri"

/**
 * Сервис, отвечающий за авторизацию и регистрацию пользователя
 */
@PerApplication
class AuthRepository @Inject
constructor(private val authApi: AuthApi) : BaseNetworkService() {

    /**
     * Отсылка номера телефона, для получения токена
     */
    fun sendPhoneNumber(phoneNumber: String): Observable<KeyInfo> {
        return authApi.loginByPhoneNumber(LoginByPhoneRequest(phoneNumber, CLIENT_ID))
                .map({ TransformUtil.transform(it) })
    }

    /**
     * Вход по полученному из смс коду
     */
    fun loginByCode(key: String, smsCode: String): Observable<LoginInfo> {
        val code = String.format(CODE_FORMAT, key, smsCode)
        return authApi.loginByCode(FALSE.toString(), getFieldsForReceiveToken(code))
                .map({ TransformUtil.transform(it) })
    }

    /**
     * Обновление токена
     */
    fun refreshToken(refreshToken: String): Observable<LoginInfo> {
        return authApi.loginByCode(TRUE.toString(), getFieldsForRefreshToken(refreshToken))
                .map({ TransformUtil.transform(it) })
    }

    /**
     * Вход только по key, для временного пользователя.
     */
    fun loginByKey(key: String): Observable<LoginInfo> {
        return authApi.loginByCode(FALSE.toString(), getFieldsForReceiveToken(key))
                .map({ TransformUtil.transform(it) })
    }

    private fun getFieldsForReceiveToken(code: String): Map<String, String> {
        return ImmutableMap.of(
                GRAND_TYPE_KEY, GRANT_TYPE_AUTH,
                CLIENT_ID_KEY, CLIENT_ID,
                CODE_KEY, code,
                REDIRECT_URI_KEY, REDIRECT_URI)
    }

    private fun getFieldsForRefreshToken(refreshToken: String): Map<String, String> {
        return ImmutableMap.of(
                GRAND_TYPE_KEY, GRANT_TYPE_REFRESH,
                CLIENT_ID_KEY, CLIENT_ID,
                REFRESH_TOKEN_KEY, refreshToken,
                REDIRECT_URI_KEY, REDIRECT_URI)
    }
}