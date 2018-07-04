package ru.surfstudio.standard.interactor.auth.network.response

import com.google.gson.annotations.SerializedName

import ru.surfstudio.android.network.Transformable
import ru.surfstudio.standard.domain.auth.phone.LoginInfo
import ru.surfstudio.standard.interactor.common.network.error.InvalidServerValuesResponse

/**
 * сущность для ответа сервера с токенами
 */
//todo Обновить или удалить класс в соответствии с нуждами приложения
data class TokenResponse(@SerializedName("access_token")
                         private val accessToken: String? = null,
                         @SerializedName("expires_in")
                         private val expiresIn: Int = 0,
                         @SerializedName("token_type")
                         private val tokenType: String? = null,
                         @SerializedName("refresh_token")
                         private val refreshToken: String? = null)
    : Transformable<LoginInfo> {
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