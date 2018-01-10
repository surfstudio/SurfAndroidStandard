package ru.surfstudio.standard.interactor.common.error


import com.google.gson.Gson

import java.io.IOException

import okhttp3.ResponseBody
import retrofit2.adapter.rxjava.HttpException
import ru.surfstudio.standard.interactor.common.response.ResultResponse
import timber.log.Timber

/**
 * получен ответ не 2xx
 */
open class HttpProtocolException(cause: HttpException, val httpMessage: String, val code: Int, url: String) : NetworkException(HttpProtocolException.prepareMessage(httpMessage, code, url, null, 0), cause) {
    val innerCode: Int //дополнительный внутренний код срвера
    val developerMessage: String?
//    val result: Map<String, Any>? // некоторые ошибки имеют результат (лол)


    init {

        val responseBody = cause.response().errorBody()
        val response = getFromError(responseBody, ResultResponse::class.java)
        if (response != null) {
            innerCode = response.errorCode
            developerMessage = response.userMessage
//            result = response.result
        } else {
            innerCode = -1
            developerMessage = null
//            result = null
        }
    }
    companion object {
        private fun prepareMessage(httpMessage: String, code: Int, url: String, developerMessage: String?, innerCode: Int): String {
            return " httpCode=" + code + "\n" +
                    ", httpMessage='" + httpMessage + "'" +
                    ", url='" + url + "'" + "\n" +
                    ", innerCode=" + innerCode +
                    ", developerMessage='" + developerMessage + "'"
        }
    }

    private fun <T> getFromError(responseBody: ResponseBody, type: Class<T>): T? {
        var jsonString: String? = null
        try {
            jsonString = responseBody.string()
        } catch (e: IOException) {
            Timber.w(e, "Не возможно распарсить ответ сервера об ошибке")
        }

        val gson = Gson()
        return gson.fromJson(jsonString, type)
    }
}
