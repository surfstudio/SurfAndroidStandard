package ru.surfstudio.android.filestorage.sample.interactor.ip.cache

import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.filestorage.BaseFileStorage
import ru.surfstudio.android.filestorage.CacheConstant.INTERNAL_CACHE_DIR_DAGGER_NAME
import ru.surfstudio.android.filestorage.ObjectConverter
import ru.surfstudio.android.filestorage.Encryptor
import ru.surfstudio.android.filestorage.naming.NamingProcessor
import ru.surfstudio.android.filestorage.processor.FileProcessor
import ru.surfstudio.android.filestorage.sample.domain.ip.Ip
import javax.inject.Inject
import javax.inject.Named

/**
 * Локальное хранилище информации об ip
 */
@PerApplication
class IpStorage @Inject constructor(@Named(INTERNAL_CACHE_DIR_DAGGER_NAME) cacheDir: String,
                                    encryptor: Encryptor,
                                    ipObjectConverter: ObjectConverter<Ip>
) : BaseFileStorage<Ip>(
        FileProcessor(cacheDir, KEY_IP_STORAGE, 1),
        NamingProcessor { rawName -> rawName },
        encryptor,
        ipObjectConverter) {

    companion object {
        private const val KEY_IP_STORAGE = "ip_storage"
    }

    var ip: Ip?
        get() = get(KEY_IP_STORAGE)
        set(value) {
            value?.let { put(KEY_IP_STORAGE, it) }
        }
}