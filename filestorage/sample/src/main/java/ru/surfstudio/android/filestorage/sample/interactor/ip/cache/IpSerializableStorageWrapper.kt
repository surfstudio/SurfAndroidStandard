package ru.surfstudio.android.filestorage.sample.interactor.ip.cache

import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.filestorage.storage.BaseSerializableFileStorage
import ru.surfstudio.android.filestorage.sample.domain.ip.Ip
import javax.inject.Inject

@PerApplication
class IpSerializableStorageWrapper @Inject constructor(
        private val ipSerializableFileStorage: BaseSerializableFileStorage<Ip>) {

    companion object {
        private const val KEY_IP_SERIALIZABLE_STORAGE = "ip_serializable_storage"
    }

    fun saveIp(ip: Ip) = ipSerializableFileStorage.put(KEY_IP_SERIALIZABLE_STORAGE, ip)

    fun getIp(): Ip? = ipSerializableFileStorage.get(KEY_IP_SERIALIZABLE_STORAGE)

    fun clear() = ipSerializableFileStorage.clear()
}