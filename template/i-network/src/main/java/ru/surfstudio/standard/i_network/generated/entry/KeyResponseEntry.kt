package ru.surfstudio.standard.i_network.generated.entry

import com.google.gson.annotations.SerializedName
import ru.surfstudio.standard.domain.entity.KeyInfoEntity
import ru.surfstudio.standard.i_network.network.Transformable

/**
 * Модель для парсинга ответа сервера с ключом
 */
//todo Обновить или удалить класс в соответствии с нуждами приложения
data class KeyResponseEntry(
        @SerializedName("key") private val key: String? = null,
        @SerializedName("expires") private val expires: String? = null
) : Transformable<KeyInfoEntity> {

    override fun transform(): KeyInfoEntity {
        return KeyInfoEntity(key, expires)
    }
}