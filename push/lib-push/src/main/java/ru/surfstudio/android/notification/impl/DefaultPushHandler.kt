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
package ru.surfstudio.android.notification.impl

import android.app.Activity
import android.content.Context
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.notification.PushHandler
import ru.surfstudio.android.notification.interactor.push.IntentPushDataConverter
import ru.surfstudio.android.notification.interactor.push.PushNotificationsListener
import ru.surfstudio.android.notification.ui.notification.AbstractPushHandleStrategyFactory
import ru.surfstudio.android.notification.ui.notification.PushHandlingActivity

/**
 * Выполняем необходимые действия при пуше на ui
 * Определяет нужно ли послать событие в пуш интерактор, либо же
 * создать нотификацию открытия экрана по пушу
 */
class DefaultPushHandler(
        private val activeActivityHolder: ActiveActivityHolder,
        private val pushHandleStrategyFactory: AbstractPushHandleStrategyFactory,
        private val pushNotificationsListener: PushNotificationsListener
) : PushHandler {

    override fun handleMessage(context: Context, title: String, body: String, data: Map<String, String>) {
        val activity = activeActivityHolder.activity
        val pushHandleStrategy = createStrategy(data)

        Logger.i("DefaultPushHandler пуш $activity \n $title \n pushStrategy = $pushHandleStrategy")
        pushHandleStrategy?.handle(activity ?: context, pushNotificationsListener, body.hashCode(), title, body)
    }

    /**
     * @see [PushHandler.handleMessage]
     */
    override fun handleMessage(context: Context,
                               uniqueId: Int,
                               title: String,
                               body: String,
                               data: Map<String, String>) {
        val activity = activeActivityHolder.activity
        val pushHandleStrategy = createStrategy(data)

        pushHandleStrategy?.handle(
                context = activity ?: context,
                pushNotificationsListener = pushNotificationsListener,
                uniqueId = uniqueId,
                title = title,
                body = body
        )
    }

    /**
     * Создание стратегии по данным из интента.
     *
     * @param data данные из нотификации
     */
    override fun createStrategy(data: Map<String, String>) =
            pushHandleStrategyFactory.createByData(data)

    /**
     * Перехват старта активити, помеченной маркерным интерфейсом [PushHandlingActivity]
     * Данный мметод должен быть добавлен в DefaultActivityLifecycleCallbacks
     */
    override fun onActivityStarted(activity: Activity) {
        if (activity is PushHandlingActivity) {
            Logger.i("PUSH HANDLE ON $activity")
            val strategy = createStrategy(IntentPushDataConverter.convert(activity.intent))

            if (strategy != null) {
                activity.startActivity(strategy.coldStartIntent(activity))

                //чтобы не было повторного перехода на активити, обнуляем данные из пуша
                activity.intent.putExtra(pushHandleStrategyFactory.key, -1)
            } else {
                Logger.i("PUSH: strategy not found for intent = ${activity.intent}")
            }
        }
    }
}