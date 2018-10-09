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

import android.content.Context
import ru.surfstudio.android.core.app.ActiveActivityHolder
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.notification.interactor.push.PushInteractor

/**
 * Выполняем необходимые действия при пуше на ui
 * Определяет нужно ли послать событие в пуш интерактор, либо же
 * создать нотификацию открытия экрана по пушу
 */
class DefaultPushHandler(
        private val activeActivityHolder: ActiveActivityHolder,
        private val pushHandleStrategyFactory: AbstractPushHandleStrategyFactory,
        private val pushInteractor: PushInteractor
) : PushHandler {

    override fun handleMessage(context: Context, title: String, body: String, data: Map<String, String>) {
        val activity = activeActivityHolder.activity

        val pushHandleStrategy = pushHandleStrategyFactory
                .createByData(data)
        Logger.i("DefaultPushHandler пуш $activity \n $title \n pushStrategy = $pushHandleStrategy")
        pushHandleStrategy?.handle(activity ?: context, pushInteractor, title, body, data)
    }
}