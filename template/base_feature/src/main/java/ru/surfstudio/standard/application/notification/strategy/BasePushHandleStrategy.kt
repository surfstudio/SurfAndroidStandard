package ru.surfstudio.standard.application.notification.strategy

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import ru.surfstudio.android.notification.interactor.push.PushInteractor
import ru.surfstudio.android.notification.ui.notification.groups.NotificationsGroup
import ru.surfstudio.android.notification.ui.notification.strategies.SimpleAbstractPushHandleStrategy
import ru.surfstudio.android.template.base_feature.R
import ru.surfstudio.android.utilktx.util.SdkUtils
import ru.surfstudio.standard.application.notification.type.NotificationTypeData
import ru.surfstudio.standard.ui.navigation.routes.MainActivityRoute
import kotlin.math.absoluteValue

/**
 * Базовая стратегия обработки push-уведомления
 * todo исправить исходя из нужд приложения
 */
class BasePushHandleStrategy : SimpleAbstractPushHandleStrategy<NotificationTypeData>() {

    override val typeData by lazy { NotificationTypeData() }

    override val channelId: Int
        get() = R.string.notification_channel_id

    override val channelName: Int
        get() = R.string.notification_channel_name

    override val icon: Int
        get() = R.mipmap.ic_launcher

    override val color: Int
        get() = R.color.colorAccent

    override val group = NotificationsGroup("Messages")

    override fun coldStartIntent(context: Context): Intent =
            MainActivityRoute().createIntent(context)

    override fun handlePushInActivity(activity: Activity) = false

    override fun handle(
        context: Context,
        pushInteractor: PushInteractor,
        uniqueId: Int,
        title: String,
        body: String
    ) {
        super.handle(
            context,
            pushInteractor,
            uniqueId.absoluteValue,
            title,
            body
        )
    }

    override fun makeNotificationBuilder(
        context: Context,
        title: String,
        body: String
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, context.getString(channelId))
            .setDefaults(0)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(icon)
            .setColor(ContextCompat.getColor(context, color))
            .setAutoCancel(autoCancelable)
            .setGroup(group.groupAlias)
            .setContentIntent(pendingIntent)
            .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun makeNotificationChannel(context: Context, title: String): NotificationChannel? {
        return if (SdkUtils.isAtLeastOreo()) {
            val id = context.resources.getString(channelId)
            val name = context.resources.getString(channelName)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            NotificationChannel(id, name, importance)
        } else {
            null
        }
    }
}