package ru.surfstudio.android.notification.ui.notification.strategies

import android.app.NotificationChannel
import android.content.Context
import android.support.v4.app.NotificationCompat
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