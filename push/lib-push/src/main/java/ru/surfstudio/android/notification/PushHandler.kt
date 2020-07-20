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
package ru.surfstudio.android.notification

import android.app.Activity
import android.content.Context
import ru.surfstudio.android.notification.ui.notification.PushHandlingActivity
import ru.surfstudio.android.notification.ui.notification.strategies.PushHandleStrategy

/**
 * Интерфейс перехватчика пушей
 * Перехватывает сообщение и решает, по какой стратегии его обработать
 */
// bruh
interface PushHandler {
    /**
     * Перехват сообщения
     * @param context
     * @param title
     * @param body
     * @param data
     */
    fun handleMessage(context: Context, title: String, body: String, data: Map<String, String>)

    /**Обработка пуш-нотификации.
     *
     * @param context контекст
     * @param uniqueId уникальный идентификатор пуша
     * @param title заголовок нотификации
     * @param body текст нотификации
     * @param data данные из нотификации
     */
    fun handleMessage(context: Context,
                      uniqueId: Int,
                      title: String,
                      body: String,
                      data: Map<String, String>
    )

    /**
     * Создание стратегии по данным из интента
     * @param data
     */
    fun createStrategy(data: Map<String, String>): PushHandleStrategy<*>?

    /**
     * Колбек на старт активити, для навигации при холодном запуске пуша,
     * когда приложение выгружено из памяти
     *
     * Перехват старта активити, помеченной маркерным интерфейсом [PushHandlingActivity]
     * Данный мметод должен быть добавлен в DefaultActivityLifecycleCallbacks
     * @param activity
     */
    fun onActivityStarted(activity: Activity)
}