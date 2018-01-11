package ru.surfstudio.standard.interactor.common.response

import com.google.gson.annotations.SerializedName

/**
 * базовый интерфейс ответа
 */

open class ErrorResponse: BaseResponse {

    @SerializedName("userMessage")
    var userMessage: String? = null
        set(userMessage) {
            field = this.userMessage
        }
    @SerializedName("errorCode")
    var errorCode: Int = 0
        set(errorCode) {
            field = this.errorCode
        }
}
