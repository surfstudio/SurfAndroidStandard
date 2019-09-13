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
package ru.surfstudio.android.notification.ui.notification.strategies

import android.app.NotificationChannel
import android.content.Context
import androidx.core.app.NotificationCompat
import android.widget.RemoteViews
import ru.surfstudio.android.notification.interactor.push.BaseNotificationTypeData

/**
 * Базовая абстрактная стратегия обработки пуша
 * и его создание в простом виде
 *
 * От нее наследуемся, если не нужно менять стандартный вид пуша,
 * и не нужно каких-либо настроек для канала(Android O [https://developer.android.com/training/notify-user/channels.html?hl=ru])
 */
abstract class SimpleAbstractPushHandleStrategy<out T : BaseNotificationTypeData<*>> : PushHandleStrategy<T>() {
    override val autoCancelable: Boolean = true
    override val contentView: RemoteViews? = null

    override fun makeNotificationBuilder(context: Context, title: String, body: String): NotificationCompat.Builder? = null
    override fun makeNotificationChannel(context: Context, title: String): NotificationChannel? = null
}