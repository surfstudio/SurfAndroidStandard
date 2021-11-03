package ru.surfstudio.standard.i_network.generated.entry

import com.google.gson.annotations.SerializedName

/**
 * сущность для запроса на получение ключа по номеру телефона
 */
//todo Обновить или удалить класс в соответствии с нуждами приложения
data class LoginByPhoneRequestEntry(
        @SerializedName("phone") private val phone: String? = null
)