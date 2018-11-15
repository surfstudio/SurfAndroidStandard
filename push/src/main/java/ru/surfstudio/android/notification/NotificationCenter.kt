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
import ru.surfstudio.android.core.app.ActiveActivityHolder
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.notification.interactor.push.IntentPushDataConverter
import ru.surfstudio.android.notification.interactor.push.PushInteractor
import ru.surfstudio.android.notification.ui.notification.strategies.storage.AbstractPushHandleStrategyFactory
import ru.surfstudio.android.notification.impl.DefaultPushHandler
import ru.surfstudio.android.notification.ui.notification.PushHandlingActivity

/**
 * Помощник инициализации нотификаций
 */
@Deprecated("Use PushHandler instead")
object NotificationCenter {

    private lateinit var activeActivityHolder: ActiveActivityHolder
    private lateinit var pushHandleStrategyFactory: AbstractPushHandleStrategyFactory

    private lateinit var pushHandler: PushHandler

    fun setActiveActivityHolder(activeActivityHolder: ActiveActivityHolder) =
            apply { this.activeActivityHolder = activeActivityHolder }

    fun setPushHandleStrategyFactory(pushHandleStrategyFactory: AbstractPushHandleStrategyFactory) =
            apply { this.pushHandleStrategyFactory = pushHandleStrategyFactory }

    fun configure(conf: NotificationCenter.() -> Unit) {
        this.conf()
        pushHandler = DefaultPushHandler(activeActivityHolder, pushHandleStrategyFactory, PushInteractor())
    }

    /**
     * Перехват старта активити, помеченной маркерным интерфейсом [PushHandlingActivity]
     * Данный мметод должен быть добавлен в DefaultActivityLifecycleCallbacks
     */
    fun onActivityStarted(activity: Activity) {
        if (activity is PushHandlingActivity) {
            Logger.i("PUSH HANDLE ON $activity")
            val strategy = pushHandleStrategyFactory.createByData(IntentPushDataConverter.convert(activity.intent))
            if (strategy != null) activity.startActivity(strategy.coldStartRoute().prepareIntent(activity))
        }
    }

    /**
     * Обработка сообщения из FirebaseMessagingService
     */
    fun onReceiveMessage(context: Context, title: String, body: String, data: Map<String, String>) {
        pushHandler.handleMessage(context, -1, title, body, data)
    }
}