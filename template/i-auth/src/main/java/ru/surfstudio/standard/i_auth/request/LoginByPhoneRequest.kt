package ru.surfstudio.standard.i_auth.request

import com.google.gson.annotations.SerializedName

/**
 * сущность для запроса на получение ключа по номеру телефона
 */
//todo Обновить или удалить класс в соответствии с нуждами приложения
data class LoginByPhoneRequest(
        @SerializedName("phone") private val phone: String? = null
)