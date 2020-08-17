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

import android.app.Activity
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import android.widget.RemoteViews
import androidx.annotation.StringRes
import ru.surfstudio.android.notification.R
import ru.surfstudio.android.notification.interactor.push.BaseNotificationTypeData
import ru.surfstudio.android.notification.interactor.push.PushInteractor
import ru.surfstudio.android.notification.ui.notification.groups.NotificationsGroup
import java.io.Serializable
import ru.surfstudio.android.notification.ui.notification.*


/**
 * Стратегия обработки пуша
 *
 * Позволяет задать вид нотификации(при активном приложении в случае NotificationMessage,
 * при активном и неактивнм в случае data message)
 *
 * Для Android O необходимо указать id канала
 */
abstract class PushHandleStrategy<out T : BaseNotificationTypeData<*>> : Serializable {

    /**
     * Идентификатор пуш-нотификации. При совпадении идентификаторов пуш-нотификации заменяют друг
     * друга. Если в стратегии не переопределить данное свойство, все нотификации будут
     * автоматически получать уникальные идентификаторы.
     */
    open var pushId: Int = -1

    /**
     * тип данных пуша [BaseNotificationTypeData]
     */
    abstract val typeData: T

    /**
     * Группа пуш-нотификации.
     */
    open val group: NotificationsGroup? = null

    /**
     * Id канала пуш уведомлений(строковый ресурс)
     */
    abstract val channelId: Int

    /**
     * Имя канала
     */
    @StringRes
    open val channelName: Int = R.string.default_channel_name

    /**
     * Иконка
     */
    abstract val icon: Int

    /**
     * Цвет иконки пуш уведомлений(ресурс)
     */
    abstract val color: Int

    /**
     * Флаг автозакрытия
     */
    abstract val autoCancelable: Boolean

    /**
     * Кастомный вид пуш-уведомления
     */
    abstract val contentView: RemoteViews?

    /**
     * Кастомный builder для нотификаций.
     */
    var channel: NotificationChannel? = null

    /**
     * Кастомный builder для заголовка группы нотификаций.
     */
    var groupSummaryNotificationBuilder: NotificationCompat.Builder? = null

    /**
     * Кастомный builder для пушей(если нужно какое-то особое поведение)
     */
    var notificationBuilder: NotificationCompat.Builder? = null

    /**
     * Действия при нажатии на пуш
     */
    lateinit var pendingIntent: PendingIntent

    /**
     * Действия при отмене пуш
     */
    lateinit var deleteIntent: PendingIntent

    /**
     * Определяем в каких случаях пуш не надо отображать,
     * а необходимо выполнить те или иные действия по подписке
     */
    abstract fun handlePushInActivity(activity: Activity): Boolean

    /**
     * Интент при нажатии на пуш, если приложение в бэкграунде
     */
    abstract fun coldStartIntent(context: Context): Intent?

    /**
     * Метод для инициализации билдера нотификаций.
     *
     * @param context контекст
     * @param title заголовок пуш-нотификации
     * @param body текст пуш-нотификации
     */
    abstract fun makeNotificationBuilder(context: Context, title: String, body: String): NotificationCompat.Builder?

    /**
     * Метод для создания канала нотификаций
     */
    abstract fun makeNotificationChannel(context: Context, title: String): NotificationChannel?

    /**
     * Требуемое действие нотификации
     *
     * @param context текущий контекст
     * @param pushInteractor пуш-интерактор
     * @param uniqueId уникальный идентификатор для уведомления, будет использован если не задан [pushId]
     * @param title заголовок пуш-нотификации
     * @param body текст пуш-нотификации
     */
    open fun handle(
            context: Context,
            pushInteractor: PushInteractor,
            uniqueId: Int,
            title: String,
            body: String
    ) {

        pendingIntent = preparePendingIntent(context, title, group?.id)
        channel = makeNotificationChannel(context, title)
        notificationBuilder = makeNotificationBuilder(context, title, body)
        groupSummaryNotificationBuilder = makeGroupSummaryNotificationBuilder(context, title, body)
        pushId = makePushId(uniqueId)
        deleteIntent = makeDeleteIntent(context, group?.id)
        pushInteractor.onNewNotification(typeData)

        if (context is Activity && handlePushInActivity(context)) return

        showNotification(context, pushId, title, body)
    }

    /**
     * Требуемое действие нотификации
     *
     * @param context текущий контекст
     * @param pushInteractor пуш-интерактор
     * @param title заголовок пуш-нотификации
     * @param body текст пуш-нотификации
     */
    @Deprecated("Используйте метод с uniqueId",
            ReplaceWith("handle(context, pushInteractor, uniqueId, title, body"))
    open fun handle(
            context: Context,
            pushInteractor: PushInteractor,
            title: String,
            body: String
    ) {

        pendingIntent = preparePendingIntent(context, title, -1)
        notificationBuilder = makeNotificationBuilder(context, title, body)
        channel = makeNotificationChannel(context, title)
        pushInteractor.onNewNotification(typeData)

        if (context is Activity && handlePushInActivity(context)) return

        showNotification(context, title, body)
    }

    /**
     * Метод для инициализации builder'а заголовка группы нотификаций.
     */
    open fun makeGroupSummaryNotificationBuilder(context: Context,
                                                 title: String,
                                                 body: String): NotificationCompat.Builder? {
        return null
    }

    /**
     * Метод для создания сводку для группы уведомлений
     * Используется для API_LEVEL <= 23
     * @param notificationCount количество уведомлений в группе. Всегда больше 1
     * @return сводка строки
     */
    open fun makeGroupSummary(notificationCount: Int): String {
        return "$notificationCount Messages"
    }

    /**
     * Интент в соответствии с необходимыми действиями
     */
    private fun preparePendingIntent(context: Context, title: String, groupId: Int?): PendingIntent {
        val intent = Intent(context, NotificationClickEventReceiver::class.java)
        intent.putExtra(NOTIFICATION_DATA, typeData)
        intent.putExtra(EVENT_TYPE_ORDINAL, Event.OPEN.ordinal)
        intent.putExtra(NOTIFICATION_GROUP_ID, groupId ?: 0)
        return PendingIntent.getBroadcast(context.applicationContext,
                title.hashCode(), intent, PendingIntent.FLAG_ONE_SHOT)
    }

    private fun makeDeleteIntent(context: Context, groupId: Int?): PendingIntent {
        val intent = Intent(context, NotificationClickEventReceiver::class.java)
        intent.putExtra(NOTIFICATION_DATA, typeData)
        intent.putExtra(EVENT_TYPE_ORDINAL, Event.DISMISS.ordinal)
        intent.putExtra(NOTIFICATION_GROUP_ID, groupId ?: 0)
        return PendingIntent.getBroadcast(context.applicationContext,
                0, intent, PendingIntent.FLAG_ONE_SHOT)
    }

    /**
     * Инициализация идентификатора пуш-нотификации.
     *
     * Если ID пуша не задано явно в стратегии, все пуши будут иметь уникальный ID.
     */
    private fun makePushId(uniqueId: Int): Int {
        return if (pushId == -1) uniqueId else pushId
    }

    private fun showNotification(context: Context, pushId: Int, title: String, body: String) {
        NotificationCreateHelper.showNotification(
                context,
                this,
                pushId,
                title,
                body
        )
    }

    private fun showNotification(context: Context, title: String, body: String) {
        NotificationCreateHelper.showNotification(
                context,
                this,
                title,
                body
        )
    }
}