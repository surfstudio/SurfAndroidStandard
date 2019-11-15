package ru.surfstudio.standard.i_network.network.response

import com.google.gson.annotations.SerializedName

/**
 * Пример объект ошибки сервера
 */
class ErrorObj(
        @SerializedName("code")
        override val code: String? = null,
        @SerializedName("status")
        override val status: String? = null,
        @SerializedName("message")
        override val message: String? = null,
        @SerializedName("displayMessage")
        override val displayMessage: String? = null
) : BaseErrorObj
