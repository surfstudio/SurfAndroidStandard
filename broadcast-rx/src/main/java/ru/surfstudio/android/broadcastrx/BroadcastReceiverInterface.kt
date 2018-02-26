package ru.surfstudio.android.broadcastrx

import android.content.BroadcastReceiver

/**
 * Интерфейс реактивного броадкаст рессиврера
 */
interface BroadcastReceiverInterface {

    /**
     * Регистрация броадкаст рессивера
     */
    fun registerBroadcastReceiver(broadcastReceiver: BroadcastReceiver)

    /**
     * Отписка от броадкаст рессивера
     */
    fun unregisterBroadcastReceiver(broadcastReceiver: BroadcastReceiver)
}