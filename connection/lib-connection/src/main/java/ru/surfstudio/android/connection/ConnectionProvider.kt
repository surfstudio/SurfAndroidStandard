package ru.surfstudio.android.connection

import android.content.Context
import android.net.NetworkInfo
import io.reactivex.Observable

/**
 * Provider, позволяющий подписаться на событие изменения состояния соединения
 */
interface ConnectionProvider {

    fun observeConnectionChanges(): Observable<Boolean>

    fun isConnected(): Boolean

    fun isDisconnected(): Boolean

    fun isConnectedFast(): Boolean

    /**
     * Проверка на подключение к Wi-Fi
     *
     * @return подключен ли девайс к Wi-Fi, или к мобильной сети
     */
    fun isConnectedToWifi(): Boolean

    fun getNetworkInfo(context: Context): NetworkInfo?
}