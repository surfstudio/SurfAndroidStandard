/*
  Copyright (c) 2018-present, SurfStudio LLC, Akhbor Akhrorov.

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
package ru.surfstudio.android.notification.ui

import android.content.Context
import android.content.Intent

/**
 * Интерфейс обработки событий пуша
 *
 * Interface for handling push events (open, dismiss)
 */
interface PushEventListener {

    /**
     * Вызывается при тапе на пуш
     *
     * Will be call on push clicked (opened)
     */
    fun pushOpenListener(context: Context, intent: Intent)

    /**
     * Вызывается когда пуш отменено
     *
     * Will be call on push dismissed
     */
    fun pushDismissListener(context: Context, intent: Intent)

    /**
     * Вызывается, когда нажата кастомная кнопка
     *
     * Will be called on custom action click
     */
    fun customActionListener(context: Context, intent: Intent)
}