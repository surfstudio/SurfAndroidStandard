package ru.surfstudio.standard.i_auth.response

import com.google.gson.annotations.SerializedName
import ru.surfstudio.standard.domain.login.KeyInfo
import ru.surfstudio.android.network.Transformable

/**
 * Модель для парсинга ответа сервера с ключом
 */
//todo Обновить или удалить класс в соответствии с нуждами приложения
data class KeyResponse(
        @SerializedName("key") private val key: String? = null,
        @SerializedName("expires") private val expires: String? = null
) : Transformable<KeyInfo> {

    override fun transform(): KeyInfo {
        return KeyInfo(key, expires)
    }
}