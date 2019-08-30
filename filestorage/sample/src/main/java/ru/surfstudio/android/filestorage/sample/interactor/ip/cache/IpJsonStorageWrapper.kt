package ru.surfstudio.android.filestorage.sample.interactor.ip.cache

import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.filestorage.storage.BaseJsonFileStorage
import ru.surfstudio.android.filestorage.sample.domain.ip.Ip
import javax.inject.Inject

@PerApplication
class IpJsonStorageWrapper @Inject constructor(
        private val ipJsonFileStorage: BaseJsonFileStorage<Ip>) {

    companion object {
        private const val KEY_IP_JSON_STORAGE = "ip_json_storage"
    }

    fun saveIp(ip: Ip) = ipJsonFileStorage.put(KEY_IP_JSON_STORAGE, ip)

    fun getIp(): Ip? = ipJsonFileStorage.get(KEY_IP_JSON_STORAGE)

    fun clear() = ipJsonFileStorage.clear()
}