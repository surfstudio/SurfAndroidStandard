package ru.surfstudio.android.broadcastextension

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.telephony.SmsMessage
import io.reactivex.Observable
import ru.surfstudio.android.broadcastrx.RxBroadcastReceiver


/**
 * Бродкаст слушает входящие СМС
 */
class SmsBroadcastReceiver constructor(private val context: Context) {

    private val SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED"
    private val SMS_BUNDLE = "pdus"

    companion object {
        fun create(context: Context) = SmsBroadcastReceiver(context).createSmsBroadcast()
    }

    private fun createSmsBroadcast(): Observable<String> {
        return RxBroadcastReceiver(context, IntentFilter(SMS_RECEIVED)).createBroadcast()
                .map {
                    parseSms(it)
                }
    }

    /**
     * Метод парсит СМС из интента
     * @return Текст СМС'ки
     */
    private fun parseSms(intent: Intent): String? {
        var message: String? = null
        val intentExtras = intent.extras
        if (intentExtras != null) {
            val sms = intentExtras.get(SMS_BUNDLE) as Array<*>
            for (sm in sms) {
                val smsMessage: SmsMessage
                smsMessage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val format = intentExtras.getString("format")
                    SmsMessage.createFromPdu(sm as ByteArray, format)
                } else {
                    SmsMessage.createFromPdu(sm as ByteArray)
                }
                message = smsMessage.messageBody
            }
        }
        return message
    }
}