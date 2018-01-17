package ru.surfstudio.standard.interactor.auth.network

import retrofit2.http.*
import ru.surfstudio.standard.interactor.auth.IS_REFRESHING_TOKEN_KEY
import ru.surfstudio.standard.interactor.auth.network.request.LoginByPhoneRequest
import ru.surfstudio.standard.interactor.auth.network.response.KeyResponse
import ru.surfstudio.standard.interactor.auth.network.response.TokenResponse
import rx.Observable

/**
 * Api для авторизации
 */
interface AuthApi {

    @POST(SEND_PHONE_PATH)
    fun loginByPhoneNumber(@Body phoneRequest: LoginByPhoneRequest): Observable<KeyResponse>

    @FormUrlEncoded
    @POST(GET_TOKEN_URL)
    fun loginByCode(@Header(IS_REFRESHING_TOKEN_KEY) isRefreshing: String, @FieldMap fields: Map<String, String>): Observable<TokenResponse>

}