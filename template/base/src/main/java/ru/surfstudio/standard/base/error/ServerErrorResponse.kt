package ru.surfstudio.standard.base.error

import com.google.gson.annotations.SerializedName

/**
 * Специфичный ответ сервера в случае 400 ошибки
 * todo исправить в соответствии с реальным ответом сервера
 */
data class ServerErrorResponse (
    @SerializedName("code") val code: Int,
    @SerializedName("data") val data: String
)