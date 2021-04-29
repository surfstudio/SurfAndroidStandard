package ru.surfstudio.android.core.mvp.binding.test

import android.content.Context
import android.net.NetworkInfo
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import ru.surfstudio.android.connection.ConnectionProvider

/**
 * Тестовая реализация [ConnectionProvider]
 */
class TestConnectionProvider : ConnectionProvider {

    private val connectionSubject = PublishSubject.create<Boolean>()

    private var isConnectedInternal: Boolean = DEFAULT_IS_CONNECTED
    private var isConnectionFastInternal: Boolean = DEFAULT_IS_CONNECTION_FAST
    private var isConnectedToWifiInternal: Boolean = DEFAULT_IS_CONNECTED_TO_WIFI

    var networkInfo: NetworkInfo? = null

    fun reset() {
        isConnectedInternal = DEFAULT_IS_CONNECTED
        isConnectionFastInternal = DEFAULT_IS_CONNECTION_FAST
        isConnectedToWifiInternal = DEFAULT_IS_CONNECTED_TO_WIFI
    }

    fun emitConnectionChanged(isConnected: Boolean) {
        isConnectedInternal = isConnected
        connectionSubject.onNext(isConnected)
    }

    fun setIsConnectionFast(isConnectionFast: Boolean) {
        isConnectionFastInternal = isConnectionFast
    }

    fun setIsConnectedToWifi(isConnectedToWifi: Boolean) {
        isConnectedToWifiInternal = isConnectedToWifi
    }

    override fun observeConnectionChanges(): Observable<Boolean> = connectionSubject.hide()

    override fun isConnected(): Boolean = isConnectedInternal

    override fun isDisconnected(): Boolean = !isConnected()

    override fun isConnectedFast(): Boolean = isConnectionFastInternal

    override fun isConnectedToWifi(): Boolean = isConnectedToWifiInternal

    override fun getNetworkInfo(context: Context): NetworkInfo? = networkInfo

    companion object {
        const val DEFAULT_IS_CONNECTED = true
        const val DEFAULT_IS_CONNECTION_FAST = true
        const val DEFAULT_IS_CONNECTED_TO_WIFI = true
    }
}