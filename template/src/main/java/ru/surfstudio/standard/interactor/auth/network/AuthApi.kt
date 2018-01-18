package ru.surfstudio.standard.interactor.auth.network

import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import ru.surfstudio.standard.domain.auth.phone.LoginInfo
import ru.surfstudio.standard.interactor.auth.network.request.*
import ru.surfstudio.standard.interactor.auth.network.response.KeyResponse
import ru.surfstudio.standard.interactor.auth.network.response.RecoverByEmailResponse
import ru.surfstudio.standard.interactor.auth.network.response.RecoverByPhoneResponse
import ru.surfstudio.standard.interactor.auth.network.response.TokenResponse
import rx.Observable

/**
 * Api для авторизации
 */
interface AuthApi {

    @POST(LOGIN_BY_PHONE_URL)
    fun requestCode(@Body phoneRequest: LoginByPhoneRequest): Observable<KeyResponse>

    @FormUrlEncoded
    @POST(GET_TOKEN_URL)
    fun loginByCode(@Body request: LoginByCodeRequest): Observable<TokenResponse>

    @POST(LOGIN_BY_EMAIL_URL)
    fun loginByEmail(@Body phoneRequest: LoginByEmailRequest): Observable<LoginInfo>

    @POST(USER_LOGOUT_URL)
    fun logout(): Observable<Unit>

    @POST(RECOVER_ACCESS_BY_EMAIL_URL)
    fun recoverByEmail(@Body request: RecoverByEmailRequest): Observable<RecoverByEmailResponse>

    @POST(RECOVER_ACCESS_BY_PHONE_URL)
    fun recoverByPhone(@Body request: RecoverByPhoneRequest): Observable<RecoverByPhoneResponse>
}