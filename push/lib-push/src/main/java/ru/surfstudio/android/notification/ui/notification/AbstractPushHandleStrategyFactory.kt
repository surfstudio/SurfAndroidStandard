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
package ru.surfstudio.android.notification.ui.notification

import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.notification.ui.notification.strategies.PushHandleStrategy
import java.util.*

/**
 * Фабрика стратегий обработки пуша по его типу
 */
abstract class AbstractPushHandleStrategyFactory {

    /**
     * ключ события в data firebase'вского пуша
     *
     * Можно настроить свой формат в релизации фабрики
     */
    var key = "event"

    /**
     * Переопределяем с необходимым соответствием действий(типа пуша) и стратегий
     */
    abstract val map: HashMap<String, PushHandleStrategy<*>>

    /**
     * Возвращает стратегию по данным пуша
     */
    fun createByData(data: Map<String, String>): PushHandleStrategy<*>? {
        Logger.d("data : ${data[key]}")
        return map[data[key]].apply {
            this?.typeData?.setDataFromMap(data)
        }
    }
}