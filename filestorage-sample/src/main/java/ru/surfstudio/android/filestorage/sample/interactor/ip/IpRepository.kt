package ru.surfstudio.android.filestorage.sample.interactor.ip

import io.reactivex.Observable
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.filestorage.sample.domain.ip.Ip
import ru.surfstudio.android.filestorage.sample.interactor.ip.cache.IpJsonStorage
import ru.surfstudio.android.filestorage.sample.interactor.ip.cache.IpSerializableStorage
import ru.surfstudio.android.filestorage.sample.interactor.ip.network.IpApi
import ru.surfstudio.android.network.BaseNetworkInteractor
import ru.surfstudio.android.network.TransformUtil
import javax.inject.Inject

/**
 * Репозиторий для получения и кэширования ip
 */
@PerApplication
class IpRepository @Inject constructor(connectionQualityProvider: ConnectionProvider,
                                       private val ipApi: IpApi,
                                       private val ipJsonStorage: IpJsonStorage,
                                       private val ipSerializableStorage: IpSerializableStorage
) : BaseNetworkInteractor(connectionQualityProvider) {

    fun getIp(): Observable<Ip> {
        return ipApi.getIp()
                .map { ipResponse -> TransformUtil.transform(ipResponse) }
    }

    fun getIpFromJsonCache(): Ip? = ipJsonStorage.ip

    fun saveIpToJsonCache(ip: Ip) {
        ipJsonStorage.ip = ip
    }

    fun getIpFromSerializableCache(): Ip? = ipSerializableStorage.ip

    fun saveIpToSerializableCache(ip: Ip) {
        ipSerializableStorage.ip = ip
    }

    /**
     * Очистка кэша
     */
    fun clearIpStorage() {
        ipSerializableStorage.clear()
    }
}