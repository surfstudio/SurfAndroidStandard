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

import java.io.Serializable

/**
 * Тип пуш уведомления с данными
 *
 * Конкретный наследник этого класса используется для извлечения конкретных данных
 */
abstract class BaseNotificationTypeData<T : Serializable> : Serializable {

    var data: T? = null

    /**
     * Извлечение данных из параметров
     *
     * @return извлекает данные из параметров
     */
    abstract fun extractData(map: Map<String, String>): T?

    /**
     * Установка данных из параметров
     */
    fun setDataFromMap(map: Map<String, String>) {
        data = extractData(map)
    }
}