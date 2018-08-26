package ru.surfstudio.android.shared.pref.sample.interactor.ip

import io.reactivex.Observable
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.shared.pref.sample.domain.ip.Ip
import ru.surfstudio.android.shared.pref.sample.interactor.ip.cache.IpStorage
import ru.surfstudio.android.shared.pref.sample.interactor.ip.network.IpApi
import ru.surfstudio.android.network.BaseNetworkInteractor
import ru.surfstudio.android.network.TransformUtil
import javax.inject.Inject

/**
 * Репозиторий для получения ip
 */
@PerApplication
class IpRepository @Inject constructor(connectionQualityProvider: ConnectionProvider,
                                       private val ipApi: IpApi,
                                       private val ipStorage: IpStorage
) : BaseNetworkInteractor(connectionQualityProvider) {

    fun getIp(): Observable<Ip> {
        return ipApi.getIp()
                .map { ipResponse -> TransformUtil.transform(ipResponse) }
    }

    fun getIpFromStorage(): Ip? = ipStorage.getIp()

    fun saveIp(ip: Ip) = ipStorage.setIp(ip)

    fun clearIpStorage() = ipStorage.clearIp()
}