package ru.surfstudio.android.filestorage

import android.util.Base64
import ru.surfstudio.android.filestorage.naming.NamingProcessor
import ru.surfstudio.android.filestorage.processor.CacheFileProcessor

/**
 * Базовый класс кэша, шифрующего данные с использованием Base64
 */
class BaseSecureLocalCache(cacheFileProccessor: CacheFileProcessor,
                              namingProcessor: NamingProcessor
) : BaseLocalCache<String>(cacheFileProccessor, namingProcessor) {

    override fun getConverter() = object : ObjectConverter<String> {

        override fun encode(value: String): ByteArray = Base64.decode(value, Base64.NO_WRAP)

        override fun decode(rawValue: ByteArray?): String = Base64.encodeToString(rawValue, Base64.NO_WRAP)
    }
}