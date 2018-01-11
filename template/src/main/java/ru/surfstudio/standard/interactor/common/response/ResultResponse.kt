package ru.surfstudio.standard.interactor.common.response

import com.google.gson.annotations.SerializedName

/**
 * Ответ от сервера содержащий результат
 */

class ResultResponse<T> : ErrorResponse() {

    @SerializedName("result")
    var result: T? = null
        set(result) {
            field = this.result
        }
}
