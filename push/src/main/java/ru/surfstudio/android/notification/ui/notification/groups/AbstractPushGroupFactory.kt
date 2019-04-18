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
@file:Suppress("unused")

package ru.surfstudio.android.notification.ui.notification.groups

import java.util.*

/**
 * Фабрика групп пуш-нотификаций.
 *
 * Группы ассоциируются со стратегями и хранятся в [groupStrategyMap].
 *
 * В проекте необходимо унаследоваться от класса [AbstractPushGroupFactory], переопределить
 * [groupStrategyMap] и проинициализировать её данными.
 */
abstract class AbstractPushGroupFactory {

    /**
     * Стратегии ассоциированные с группами пуш-нотификаций
     */
    abstract val groupStrategyMap: HashMap<Class<*>, NotificationsGroup>

    /**
     * Получение группы пуш-нотификации по стратегии.
     *
     * @param strategy стратегия, для которой требуется получить группу
     */
    fun getNotificationGroup(strategy: Class<*>): NotificationsGroup? {
        return groupStrategyMap[strategy]
    }
}
