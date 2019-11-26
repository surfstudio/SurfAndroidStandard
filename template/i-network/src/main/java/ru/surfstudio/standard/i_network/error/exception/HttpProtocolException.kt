package ru.surfstudio.standard.i_network.error.exception


import com.google.gson.Gson
import retrofit2.HttpException
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import ru.surfstudio.standard.i_network.error.ApiErrorCode
import ru.surfstudio.standard.i_network.error.exception.converters.BaseErrorResponseConverter
import ru.surfstudio.standard.i_network.network.error.NetworkException
import ru.surfstudio.standard.i_network.network.response.BaseErrorObjResponse
import java.io.IOException

/**
 * Базовая ошибка при получении ответа не 2xx с разобранными полями ответа;
 * может содержать в себе исходный [HttpException];
 * не бросается в чистом виде, идёт в составе [BaseWrappedHttpException]
 */
open class HttpProtocolException(
        message: String?,
        cause: Throwable?
) : NetworkException(message, cause) {

    var url: String = EMPTY_STRING
    var method: String = EMPTY_STRING
    var httpCode: Int = ApiErrorCode.UNKNOWN.code
    var httpMessage: String = EMPTY_STRING
    /**
     * дополнительный внутренний код сервера
     */
    var innerCode: String = EMPTY_STRING
    var errorBodyString: String = EMPTY_STRING
    var displayMessage: String = EMPTY_STRING
    var serverMessage: String = EMPTY_STRING

    private constructor(builder: Builder<out HttpProtocolException, out BaseErrorObjResponse<*>>) :
            this(prepareMessage(builder.httpMessage, builder.httpCode, builder.url, builder.serverMessage, builder.innerCode), builder.cause) {
        url = builder.url
        method = builder.method
        httpCode = builder.httpCode
        httpMessage = builder.httpMessage
        innerCode = builder.innerCode
        errorBodyString = builder.errorBodyString
        displayMessage = builder.displayMessage
        serverMessage = builder.serverMessage
    }

    /**
     * конструктор копирования из [HttpProtocolException]
     */
    constructor(source: HttpProtocolException) :
            this(prepareMessage(source.httpMessage, source.httpCode, source.url, source.serverMessage, source.innerCode), source.cause) {
        url = source.url
        method = source.method
        httpCode = source.httpCode
        httpMessage = source.httpMessage
        innerCode = source.innerCode
        errorBodyString = source.errorBodyString
        displayMessage = source.displayMessage
        serverMessage = source.serverMessage
    }

    fun getApiErrorCode() = ApiErrorCode.from(httpCode)

    override fun toString(): String {
        return "HttpProtocolException(url='$url', method='$method', httpCode=$httpCode, httpMessage='$httpMessage', innerCode='$innerCode', errorBodyString='$errorBodyString', displayMessage='$displayMessage', serverMessage='$serverMessage')"
    }

    /**
     * Билдер для создания исключений
     * типа [HttpProtocolException] и его наследников
     * @param converter конкретный [BaseErrorResponseConverter] для преобразования тела
     * ошибочного респонса в указанную сущность [Response]
     */
    open class Builder<E : HttpProtocolException, Response : BaseErrorObjResponse<*>>(
            val cause: HttpException,
            converter: BaseErrorResponseConverter<Response>,
            gson: Gson
    ) {

        val innerCode: String
        val errorBodyString: String
        val displayMessage: String
        val serverMessage: String
        val url: String
        val method: String

        var httpMessage: String
        var httpCode: Int

        init {

            val rawResponse = cause.response()

            val request = rawResponse?.raw()?.request()
            val requestUrl = request?.url()
            url = requestUrl.toString()
            method = request?.method() ?: EMPTY_STRING

            val responseBody = rawResponse?.errorBody()

            errorBodyString = responseBody?.let {
                try {
                    it.string()
                } catch (e: IOException) {
                    Logger.e("An IOException occurred during string(): $e")
                    null
                }
            } ?: EMPTY_STRING

            val response: Response? = converter.convert(gson, url, errorBodyString)

            if (response?.errorObj != null) {
                innerCode = response.errorObj?.code ?: EMPTY_STRING
                serverMessage = response.errorObj?.message ?: EMPTY_STRING
                displayMessage = response.errorObj?.displayMessage ?: EMPTY_STRING
            } else {
                innerCode = EMPTY_STRING
                serverMessage = EMPTY_STRING
                displayMessage = EMPTY_STRING
            }

            this.httpMessage = cause.message()
            this.httpCode = cause.code()
        }

        fun setHttpMessage(message: String): Builder<E, Response> {
            this.httpMessage = message
            return this
        }

        fun setHttpCode(code: Int): Builder<E, Response> {
            this.httpCode = code
            return this
        }

        open fun build() = HttpProtocolException(this)
    }

    companion object {

        private fun prepareMessage(httpMessage: String, code: Int, url: String, developerMessage: String?, innerCode: String): String {
            return " httpCode=" + code + "\n" +
                    ", httpMessage='" + httpMessage + "'" +
                    ", url='" + url + "'" + "\n" +
                    ", innerCode=" + innerCode +
                    ", serverMessage='" + developerMessage + "'"
        }
    }
}
