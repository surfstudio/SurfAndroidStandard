/*
  Copyright (c) 2018-present, SurfStudio LLC, Fedor Atyakshin, Artem Zaytsev.

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
package ru.surfstudio.android.notification.interactor.push

import android.content.Intent
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import java.util.*

/**
 * Конвертирует данные пуша из интента в мапу
 */
object IntentPushDataConverter {

    fun convert(intent: Intent): Map<String, String> {
        val extras = intent.extras ?: return HashMap()

        val data = HashMap<String, String>(extras.size())
        extras.keySet()
                .forEach { key: String ->
                    data[key] = extras[key]?.toString() ?: EMPTY_STRING
                }
        return data
    }
}
