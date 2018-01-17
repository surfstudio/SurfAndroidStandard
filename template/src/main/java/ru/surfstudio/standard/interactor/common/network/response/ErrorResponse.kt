package ru.surfstudio.standard.interactor.common.network.response

import com.google.gson.annotations.SerializedName

import ru.surfstudio.android.core.app.interactor.common.network.response.BaseResponse

/**
 * базовый интерфейс ответа
 */

data class ErrorResponse(@SerializedName("error")
                         val errorObj: ErrorObj? = null)
    : BaseResponse