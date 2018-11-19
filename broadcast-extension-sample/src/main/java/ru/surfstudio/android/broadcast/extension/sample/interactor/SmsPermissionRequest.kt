package ru.surfstudio.android.broadcast.extension.sample.interactor

import android.Manifest.permission.*
import ru.surfstudio.android.core.ui.permission.PermissionRequest

/**
 * Запрос разрешения на отправку SMS
 */
class SmsPermissionRequest : PermissionRequest() {

    override val permissions: Array<String>
        get() = arrayOf(SEND_SMS, RECEIVE_SMS, READ_SMS)
}