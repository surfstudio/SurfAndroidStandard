package ru.surfstudio.android.filestorage.sample.interactor.ip

import io.reactivex.Observable
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.filestorage.sample.domain.ip.Ip
import ru.surfstudio.android.filestorage.sample.interactor.ip.cache.IpJsonStorageWrapper
import ru.surfstudio.android.filestorage.sample.interactor.ip.cache.IpSerializableStorageWrapper
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
                                       private val ipJsonStorageWrapper: IpJsonStorageWrapper,
                                       private val ipSerializableStorageWrapper: IpSerializableStorageWrapper
) : BaseNetworkInteractor(connectionQualityProvider) {

    fun getIp(): Observable<Ip> {
        return ipApi.getIp()
                .map { ipResponse -> TransformUtil.transform(ipResponse) }
    }

    fun getIpFromJsonCache(): Ip? = ipJsonStorageWrapper.getIp()

    fun saveIpToJsonCache(ip: Ip) = ipJsonStorageWrapper.saveIp(ip)

    fun getIpFromSerializableCache(): Ip? = ipSerializableStorageWrapper.getIp()

    fun saveIpToSerializableCache(ip: Ip) = ipSerializableStorageWrapper.saveIp(ip)

    /**
     * Очистка кэша
     */
    fun clearIpStorage() {
        ipSerializableStorageWrapper.clear()
        ipJsonStorageWrapper.clear()
    }
}