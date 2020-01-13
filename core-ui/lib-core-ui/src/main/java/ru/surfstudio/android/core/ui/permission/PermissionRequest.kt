/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Tuev.

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
package ru.surfstudio.android.core.ui.permission

import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithResultRoute
import ru.surfstudio.android.core.ui.permission.screens.settings_rational.SettingsRationalDialogParams

private const val MAX_REQUEST_CODE = 32768

/**
 * Базовый класс запроса Runtime Permissions.
 */
abstract class PermissionRequest {

    /**
     * Запрашиваемые разрешения.
     */
    abstract val permissions: Array<String>

    val requestCode: Int
        get() = (this.javaClass.canonicalName.hashCode() and 0x7fffffff) % MAX_REQUEST_CODE

    /**
     * Показывать ли объяснение причины запроса разрешений при необходимости.
     *
     * Если задано true, то при необходимости объяснения будут выполнены следующие действия:
     * - если задан {@link #permissionsRationalRoute}, то будет совершен переход по заданному маршруту;
     * - если {@link #permissionsRationalRoute} не задан, но задан {@link #permissionsRationalStr}, то будет
     * совершен переход на стандартный диалог отображающий заданную строку;
     * - если не задано ни одно из вышеперечисленных свойств, то будет возбуждено исключение
     * [PermissionsRationalIsNotProvidedException].
     */
    var showPermissionsRational: Boolean = false
        protected set

    /**
     * Маршрут на экран объяснения причины запроса разрешений.
     */
    var permissionsRationalRoute: ActivityWithResultRoute<*>? = null
        protected set

    /**
     * Строка, отображаемая в стандартном диалоге объяснения причины запроса разрешений.
     */
    var permissionsRationalStr: String? = null
        protected set

    /**
     * Показывать ли объяснение необходимости перехода в настройки приложения.
     *
     * Необходимость в отображении диалога возникает, если при предыдущем запросе разрешений было отказано и выбрана
     * опция "Don't ask again".
     *
     * Если задано true, то при необходимости объяснения будут выполнены следующие действия:
     * - если задан {@link #settingsRationalRoute}, то будет совершен переход по заданному маршруту;
     * - если {@link #settingsRationalRoute} не задан, но задан {@link #settingsRationalDialogParams}, то будет
     * совершен переход на стандартный диалог, отображающий заданные параметры;
     * - если не задано ни одно из вышеперечисленных свойств, то будет возбуждено исключение
     * [SettingsRationalIsNotProvidedException].
     */
    var showSettingsRational: Boolean = false
        protected set

    /**
     * Маршрут на экран объяснения необходимости перехода в настройки приложения.
     */
    var settingsRationalRoute: ActivityWithResultRoute<*>? = null
        protected set

    /**
     * Параметры диалога с объяснением необходимости перехода в настройки приложения.
     */
    var settingsRationalDialogParams: SettingsRationalDialogParams? = null
        protected set
}