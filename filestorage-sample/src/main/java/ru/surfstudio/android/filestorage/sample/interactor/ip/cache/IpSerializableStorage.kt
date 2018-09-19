package ru.surfstudio.android.filestorage.sample.interactor.ip.cache

import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.filestorage.BaseSerializableFileStorage
import ru.surfstudio.android.filestorage.naming.NamingProcessor
import ru.surfstudio.android.filestorage.processor.FileProcessor
import ru.surfstudio.android.filestorage.sample.domain.ip.Ip
import javax.inject.Inject

@PerApplication
class IpSerializableStorage @Inject constructor(
        ipFileProcessor: FileProcessor,
        ipNamingProcessor: NamingProcessor
) : BaseSerializableFileStorage<Ip>(ipFileProcessor, ipNamingProcessor) {

    companion object {
        private const val KEY_IP_SERIALIZABLE_STORAGE = "ip_serializable_storage"
    }

    var ip: Ip?
        get() = get(KEY_IP_SERIALIZABLE_STORAGE)
        set(value) {
            value?.let { put(KEY_IP_SERIALIZABLE_STORAGE, it) }
        }
}