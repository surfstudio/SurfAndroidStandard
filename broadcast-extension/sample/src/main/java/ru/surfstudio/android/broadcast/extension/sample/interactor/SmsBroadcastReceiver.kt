package ru.surfstudio.android.broadcast.extension.sample.interactor

import android.content.Context
import android.telephony.SmsMessage
import ru.surfstudio.android.broadcast.extension.BaseSmsRxBroadcastReceiver
import ru.surfstudio.android.dagger.scope.PerApplication
import javax.inject.Inject

@PerApplication
class SmsBroadcastReceiver @Inject constructor(
        context: Context
): BaseSmsRxBroadcastReceiver<String>(context) {

    /**
     * Парсинг СМС-сообщения. Может быть добавлена дополнительная логика
     */
    override fun parseSmsMessage(smsMessage: SmsMessage, fullBody: String): String? = fullBody
}