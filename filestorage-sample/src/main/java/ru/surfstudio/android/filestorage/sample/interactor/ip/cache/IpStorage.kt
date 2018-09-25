package ru.surfstudio.android.filestorage.sample.interactor.ip.cache

import com.google.gson.GsonBuilder
import ru.surfstudio.android.filestorage.BaseLocalCache
import ru.surfstudio.android.filestorage.ObjectConverter
import ru.surfstudio.android.filestorage.naming.NamingProcessor
import ru.surfstudio.android.filestorage.processor.CacheFileProcessor
import ru.surfstudio.android.filestorage.sample.domain.ip.Ip

/**
 * Локальное хранилище информации об ip
 */
class IpStorage(internalCacheDir: String
): BaseLocalCache<Ip>(
        CacheFileProcessor(internalCacheDir, KEY_IP_STORAGE, 1),
        NamingProcessor { rawName -> rawName }) {

    companion object {
        private const val KEY_IP_STORAGE = "ip_storage"
    }

    private val gsonBuilder = GsonBuilder().create()

    override fun getConverter(): ObjectConverter<Ip> = object : ObjectConverter<Ip> {

        override fun encode(value: Ip): ByteArray = gsonBuilder.toJson(value).toByteArray()

        override fun decode(rawValue: ByteArray): Ip = gsonBuilder.fromJson(String(rawValue), Ip::class.java)
    }

    var ip: Ip?
        get() = get(KEY_IP_STORAGE)
        set(value) {
            value?.let { put(KEY_IP_STORAGE, it) }
        }
}