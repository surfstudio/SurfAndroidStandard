package ru.surfstudio.standard.i_network.generated.api

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import ru.surfstudio.standard.i_network.generated.entry.KeyResponseEntry
import ru.surfstudio.standard.i_network.generated.entry.LoginByCodeRequestEntry
import ru.surfstudio.standard.i_network.generated.entry.LoginByPhoneRequestEntry
import ru.surfstudio.standard.i_network.generated.entry.TokenResponseEntry
import ru.surfstudio.standard.i_network.generated.urls.AuthUrls.GET_TOKEN_PATH
import ru.surfstudio.standard.i_network.generated.urls.AuthUrls.LOGIN_BY_PHONE_PATH
import ru.surfstudio.standard.i_network.generated.urls.AuthUrls.USER_LOGOUT_PATH

/**
 * Api для авторизации
 */
interface AuthApi {

    @POST(LOGIN_BY_PHONE_PATH)
    fun requestCode(@Body phoneRequest: LoginByPhoneRequestEntry): Single<KeyResponseEntry>

    @FormUrlEncoded
    @POST(GET_TOKEN_PATH)
    fun loginByCode(@Body request: LoginByCodeRequestEntry): Single<TokenResponseEntry>

    @POST(USER_LOGOUT_PATH)
    fun logout(): Completable
}