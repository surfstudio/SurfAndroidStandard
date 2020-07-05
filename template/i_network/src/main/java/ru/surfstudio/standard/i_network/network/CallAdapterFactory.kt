package ru.surfstudio.standard.i_network.network

import com.google.gson.Gson
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.HttpException
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import ru.surfstudio.standard.i_network.network.calladapter.BaseCallAdapterFactory
import ru.surfstudio.standard.i_network.network.error.HttpCodes
import ru.surfstudio.standard.i_network.error.*
import java.io.IOException
import kotlin.reflect.KClass

@PerApplication
class CallAdapterFactory : BaseCallAdapterFactory() {

    override fun <R> onHttpException(e: HttpException, call: Call<R>): Observable<R> {
        val response = e.response()?.raw()
        val url = response?.request?.url?.toString() ?: EMPTY_STRING

        val httpError: HttpProtocolException = when (e.code()) {
            HttpCodes.CODE_400 -> getApiException(e, url)
            HttpCodes.CODE_401 -> NonAuthorizedException(e, e.message(), e.code(), url)
            else -> OtherHttpException(e, e.code(), url)
        }

        return Observable.error<R>(httpError)
    }

    private fun getApiException(e: HttpException, url: String): HttpProtocolException {
        val responseBody = e.response()?.errorBody()
        val response: ServerErrorResponse? = getFromError(responseBody, ServerErrorResponse::class)

        return getByCode(response, e, url)
    }

    private fun <T : Any> getFromError(responseBody: ResponseBody?, type: KClass<T>): T? {
        var jsonString: String? = null
        try {
            jsonString = responseBody?.string()
        } catch (e: IOException) {
            Logger.w(e, "Невозможно распарсить ответ сервера об ошибке")
        }

        val gson = Gson()
        return gson.fromJson(jsonString, type.java)
    }

    private fun getByCode(response: ServerErrorResponse?, e: HttpException, url: String): HttpProtocolException =
            when (ApiErrorType.getByCode(response?.code)) {
                ApiErrorType.UNKNOWN -> throw IllegalStateException("Некорректный код ошибки от сервера")
            }
}
