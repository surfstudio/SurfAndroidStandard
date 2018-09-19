package ru.surfstudio.android.filestorage.sample.interactor.ip.cache

import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.filestorage.BaseJsonFileStorage
import ru.surfstudio.android.filestorage.naming.NamingProcessor
import ru.surfstudio.android.filestorage.processor.FileProcessor
import ru.surfstudio.android.filestorage.sample.domain.ip.Ip
import javax.inject.Inject

@PerApplication
class IpJsonStorage @Inject constructor(
        ipFileProcessor: FileProcessor,
        ipNamingProcessor: NamingProcessor
): BaseJsonFileStorage<Ip>(ipFileProcessor, ipNamingProcessor, Ip::class.java) {

    companion object {
        private const val KEY_IP_JSON_STORAGE = "ip_json_storage"
    }

    var ip: Ip?
        get() = get(KEY_IP_JSON_STORAGE)
        set(value) {
            value?.let { put(KEY_IP_JSON_STORAGE, it) }
        }
}