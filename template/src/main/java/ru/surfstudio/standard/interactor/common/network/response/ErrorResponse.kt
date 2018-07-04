package ru.surfstudio.standard.interactor.common.network.response

import com.google.gson.annotations.SerializedName


/**
 * базовый интерфейс ответа
 */

data class ErrorResponse(@SerializedName("error")
                         val errorObj: ErrorObj? = null)
    : BaseResponse