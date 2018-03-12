package ru.surfstudio.android.broadcastextension

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.telephony.SmsMessage
import ru.surfstudio.android.broadcastrx.RxBroadcastReceiver

/**
 * Класс для парсинга СМС
 */
abstract class SmsBroadcastReceiver<T> constructor(context: Context)
    : RxBroadcastReceiver<T?>(context) {

    private val SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED"
    private val SMS_BUNDLE = "pdus"
    private val FORMAT = "format"

    override fun parseBroadcastIntent(intent: Intent): T? {
        var message: T? = null
        val intentExtras = intent.extras
        if (intentExtras != null) {
            val sms = intentExtras.get(SMS_BUNDLE) as Array<*>
            for (sm in sms) {
                val smsMessage: SmsMessage
                smsMessage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val format = intentExtras.getString(FORMAT)
                    SmsMessage.createFromPdu(sm as ByteArray, format)
                } else {
                    SmsMessage.createFromPdu(sm as ByteArray)
                }
                message = parseSmsMessage(smsMessage)
            }
        }
        return message
    }

    override fun intentFilter() = IntentFilter(SMS_RECEIVED)

    abstract fun parseSmsMessage(smsMessage: SmsMessage): T
}