package ru.surfstudio.standard.i_auth

import io.reactivex.Completable
import ru.surfstudio.standard.i_auth.response.KeyResponse
import ru.surfstudio.standard.i_auth.response.TokenResponse
import ru.surfstudio.standard.i_network.GET_TOKEN_PATH
import ru.surfstudio.standard.i_network.LOGIN_BY_PHONE_PATH
import ru.surfstudio.standard.i_network.USER_LOGOUT_PATH
import io.reactivex.Single
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Api для авторизации
 */
interface AuthApi {

    @POST(LOGIN_BY_PHONE_PATH)
    fun requestCode(): Single<KeyResponse>

    @FormUrlEncoded
    @POST(GET_TOKEN_PATH)
    fun loginByCode(): Single<TokenResponse>

    @POST(USER_LOGOUT_PATH)
    fun logout(): Completable
}