package ru.surfstudio.standard.i_auth.response

import com.google.gson.annotations.SerializedName
import ru.surfstudio.standard.domain.login.LoginInfo
import ru.surfstudio.standard.i_network.error.exception.InvalidServerValuesResponse
import ru.surfstudio.standard.i_network.network.Transformable


/**
 * сущность для ответа сервера с токенами
 */
//todo Обновить или удалить класс в соответствии с нуждами приложения
data class TokenResponse(
        @SerializedName("access_token") private val accessToken: String? = null,
        @SerializedName("expires_in") private val expiresIn: Int = 0,
        @SerializedName("token_type") private val tokenType: String? = null,
        @SerializedName("refresh_token") private val refreshToken: String? = null
) : Transformable<LoginInfo> {

    override fun transform(): LoginInfo {
        if (accessToken == null) {
            throw InvalidServerValuesResponse(Pair("accessToken", "null"))
        }
        if (tokenType == null) {
            throw InvalidServerValuesResponse(Pair("tokenType", "null"))
        }
        if (expiresIn == 0) {
            throw InvalidServerValuesResponse(Pair("expiresIn", expiresIn.toString()))
        }
        return LoginInfo(accessToken, expiresIn, tokenType, refreshToken)
    }
}