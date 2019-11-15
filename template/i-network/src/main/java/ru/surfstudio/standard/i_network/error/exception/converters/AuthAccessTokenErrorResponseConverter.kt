package ru.surfstudio.standard.i_network.error.exception.converters

import com.google.gson.Gson
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import ru.surfstudio.standard.i_network.network.gson.GsonHelper
import ru.surfstudio.standard.i_network.network.response.ErrorObj
import ru.surfstudio.standard.i_network.network.response.ErrorResponse
import ru.surfstudio.standard.i_network.network.response.auth.BaseAuthErrorObj

/**
 * Пример реализации [BaseErrorResponseConverter], который создаёт [ErrorResponse]
 * основываясь на наличии в url [accessTokenPath]
 */
class AuthAccessTokenErrorResponseConverter<E: BaseAuthErrorObj>(
        private val accessTokenPath: String,
        private val authErrorObjClass: Class<E>
) : BaseErrorResponseConverter<ErrorResponse> {

    override fun convert(gson: Gson, url: String, errorBodyString: String): ErrorResponse? {
        return if (url.contains(accessTokenPath)) {
            val authErrorObj = GsonHelper.fromJsonObjectString(gson, errorBodyString, authErrorObjClass)
            ErrorResponse(ErrorObj(if (authErrorObj != null) authErrorObj.error else EMPTY_STRING, EMPTY_STRING))
        } else {
            GsonHelper.fromJsonObjectString(gson, errorBodyString, ErrorResponse::class.java)
        }
    }
}