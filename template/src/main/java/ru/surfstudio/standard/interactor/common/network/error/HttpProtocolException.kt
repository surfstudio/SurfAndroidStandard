package ru.surfstudio.standard.interactor.common.network.error


import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.HttpException
import ru.surfstudio.android.network.error.NetworkException
import ru.surfstudio.standard.interactor.auth.network.GET_TOKEN_PATH
import ru.surfstudio.standard.interactor.common.network.response.AuthErrorObj
import ru.surfstudio.standard.interactor.common.network.response.ErrorObj
import ru.surfstudio.standard.interactor.common.network.response.ErrorResponse
import timber.log.Timber
import java.io.IOException

private fun prepareMessage(httpMessage: String, code: Int, url: String, developerMessage: String?, innerCode: Int): String {
    return " httpCode=" + code + "\n" +
            ", httpMessage='" + httpMessage + "'" +
            ", url='" + url + "'" + "\n" +
            ", innerCode=" + innerCode +
            ", serverMessage='" + developerMessage + "'"
}

/**
 * получен ответ не 2xx
 */
class HttpProtocolException(cause: HttpException, val httpMessage: String, val httpCode: Int, url: String)
    : NetworkException(prepareMessage(httpMessage, httpCode, url, null, 0), cause) {
    val innerCode: String? //дополнительный внутренний код сервера
    private val serverMessage: String?

    init {
        val responseBody = cause.response().errorBody()
        val response: ErrorResponse?

        if (url.contains(GET_TOKEN_PATH)) {
            val authErrorObj = getFromError(responseBody, AuthErrorObj::class.java)
            response = ErrorResponse(ErrorObj(if (authErrorObj != null) authErrorObj.error else "", ""))
        } else {
            response = getFromError(responseBody, ErrorResponse::class.java)
        }
        if (response?.errorObj != null) {
            innerCode = response.errorObj.code
            serverMessage = response.errorObj.message
        } else {
            innerCode = ""
            serverMessage = ""
        }
    }

    private fun <T> getFromError(responseBody: ResponseBody?, type: Class<T>): T? {
        var jsonString: String? = null
        try {
            jsonString = responseBody?.string()
        } catch (e: IOException) {
            Timber.w(e, "Не возможно распарсить ответ сервера об ошибке")
        }

        val gson = Gson()
        return gson.fromJson(jsonString, type)
    }
}

