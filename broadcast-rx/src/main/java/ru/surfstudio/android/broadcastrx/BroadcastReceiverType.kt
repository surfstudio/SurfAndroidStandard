package ru.surfstudio.android.broadcastrx

import java.io.Serializable

/**
 * Тип данных пришедших по широковещательному каналу
 */
class BroadcastReceiverType <T : Serializable> : Serializable {

    var data: T? = null
}