package ru.surfstudio.android.filestorage.sample.interactor.ip.cache

import com.google.gson.GsonBuilder
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.filestorage.BaseLocalCache
import ru.surfstudio.android.filestorage.CacheConstant.INTERNAL_CACHE_DIR_DAGGER_NAME
import ru.surfstudio.android.filestorage.ObjectConverter
import ru.surfstudio.android.filestorage.naming.NamingProcessor
import ru.surfstudio.android.filestorage.processor.CacheFileProcessor
import ru.surfstudio.android.filestorage.sample.domain.ip.Ip
import javax.inject.Inject
import javax.inject.Named

/**
 * Локальное хранилище информации об ip
 */
@PerApplication
class IpStorage @Inject constructor(@Named(INTERNAL_CACHE_DIR_DAGGER_NAME) cacheDir: String
) : BaseLocalCache<Ip>(
        CacheFileProcessor(cacheDir, KEY_IP_STORAGE, 2),
        NamingProcessor { rawName -> rawName }) {

    companion object {
        private const val KEY_IP_STORAGE = "ip_storage"
        private const val KEY_IP_STORAGE_SECURE = "ip_storage_secure"
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

    var ipSecure: Ip?
        get() = getSecure(KEY_IP_STORAGE_SECURE)
        set(value) {
            value?.let { putSecure(KEY_IP_STORAGE_SECURE, it) }
        }
}