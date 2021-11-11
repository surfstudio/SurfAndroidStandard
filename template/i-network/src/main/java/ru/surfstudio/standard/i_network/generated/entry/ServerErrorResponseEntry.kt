package ru.surfstudio.standard.i_network.generated.entry

import com.google.gson.annotations.SerializedName

/**
 * Специфичный ответ сервера в случае 400 ошибки
 * todo исправить в соответствии с реальным ответом сервера
 */
data class ServerErrorResponseEntry (
    @SerializedName("code") val code: Int,
    @SerializedName("data") val data: String
)