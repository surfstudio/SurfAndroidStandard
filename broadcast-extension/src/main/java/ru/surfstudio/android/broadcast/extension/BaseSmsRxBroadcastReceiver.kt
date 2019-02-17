/*
  Copyright (c) 2018-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.broadcast.extension

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.telephony.SmsMessage

/**
 * Реактивный ресивер [RxBroadcastReceiver], перехватывающий и обрабатывающий смс-сообщения
 *
 * Для использования потребуются разрешения для доступа к смс.
 * Google play отслеживает использование этого разрешения и может заблокировать приложение
 * в случае не правомерного использования.
 * Подробнее прочитать об этом, а также об альтернативных способах реализации юскейсов можно здесь:
 * https://support.google.com/googleplay/android-developer/answer/9047303
 */
abstract class BaseSmsRxBroadcastReceiver<T> constructor(
        context: Context
) : RxBroadcastReceiver<T?>(context) {

    private val SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED"
    private val SMS_BUNDLE = "pdus"
    private val FORMAT = "format"

    @Suppress("deprecation")
    override fun parseBroadcastIntent(intent: Intent): T? {
        val intentExtras = intent.extras
        if (intentExtras != null) {
            val messages = intentExtras.get(SMS_BUNDLE) as Array<*>?
            val bodyBuilder = StringBuilder("") //Полное тело сообщения
            var smsMessage: SmsMessage? = null
            messages?.forEach {
                smsMessage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val format = intentExtras.getString(FORMAT)
                    SmsMessage.createFromPdu(it as ByteArray, format)
                } else {
                    SmsMessage.createFromPdu(it as ByteArray)
                }
                bodyBuilder.append(smsMessage?.messageBody)
            }
            smsMessage?.let {
                return parseSmsMessage(it, bodyBuilder.toString())
            }
        }
        return null
    }

    override fun intentFilter() = IntentFilter(SMS_RECEIVED)

    /**
     * Функция парсинга смс-сообщения
     * Для парсинга тела следует строго использовать fullBody.
     * Так как сообщения в Android разделены на блоки по 140 байт, или 70 символов кириллицы.
     * В длинных сообщениях это разделение может повлечь за собой, что требуемая для парсинга информация
     * будет разделена на части и не обработана.
     *
     * @param smsMessage данные о сообщении
     * @param fullBody полный текст сообщения
     *
     * @return T
     */
    protected abstract fun parseSmsMessage(smsMessage: SmsMessage, fullBody: String): T?

}