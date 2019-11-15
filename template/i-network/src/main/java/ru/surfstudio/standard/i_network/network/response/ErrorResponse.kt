package ru.surfstudio.standard.i_network.network.response

import com.google.gson.annotations.SerializedName

/**
 * Пример базового интерфейса ответа
 */
data class ErrorResponse(
        @SerializedName("error")
        override val errorObj: ErrorObj? = null
) : BaseErrorObjResponse<ErrorObj>()