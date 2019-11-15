package ru.surfstudio.standard.i_network.error.exception.converters

import com.google.gson.Gson
import ru.surfstudio.standard.i_network.network.response.BaseErrorObjResponse

/**
 * Базовый конвертер тела ошибки в указанный [Response]
 */
interface BaseErrorResponseConverter<Response : BaseErrorObjResponse<*>> {

    fun convert(gson: Gson, url: String, errorBodyString: String): Response?
}