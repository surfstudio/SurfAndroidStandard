package ru.surfstudio.android.firebase.sample.ui.common.notification.strategies.simple

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import ru.surfstudio.android.firebase.sample.R
import ru.surfstudio.android.notification.interactor.push.BaseNotificationTypeData
import ru.surfstudio.android.notification.interactor.push.PushInteractor
import ru.surfstudio.android.notification.ui.notification.strategies.SimpleAbstractPushHandleStrategy
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import kotlin.math.absoluteValue

/**
 * Базовый класс для пушей, основанных на [SimpleAbstractPushHandleStrategy],
 * определяющий их внешний вид и общие параметры
 */
abstract class BaseSimplePushStrategy<out T : BaseNotificationTypeData<*>>
    : SimpleAbstractPushHandleStrategy<T>() {

    override val channelId: Int
        get() = R.string.notification_channel_id
    override val icon: Int
        get() = R.drawable.ic_android_black_24dp
    override val color: Int
        get() = R.color.colorPrimary

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

    override fun makeNotificationBuilder(context: Context, title: String, body: String): NotificationCompat.Builder? {

        return NotificationCompat.Builder(context, context.getString(channelId))
                .setDefaults(0)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(icon)
                .setColor(ContextCompat.getColor(context, color))
                .setGroup(group?.groupAlias)
                .setAutoCancel(autoCancelable)
                .setContentIntent(pendingIntent)
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY)
    }

    override fun makeNotificationChannel(context: Context, title: String): NotificationChannel? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    context.getString(channelId),
                    context.getString(channelName), //Название канала обязательно не пустое
                    NotificationManager.IMPORTANCE_HIGH
            )
            channel.enableLights(true)
            channel.enableVibration(true)
            channel
        } else {
            null
        }
    }

    override fun makeGroupSummaryNotificationBuilder(context: Context, title: String, body: String): NotificationCompat.Builder? {
        return NotificationCompat.Builder(context, context.getString(channelId))
                .setContentTitle(EMPTY_STRING)
                //set content text to support devices running API level < 24
                .setContentText(EMPTY_STRING)
                .setSmallIcon(icon)
                //specify which group this notification belongs to
                .setGroup(group?.groupAlias)
                .setAutoCancel(autoCancelable)
                //set this notification as the summary for the group
                .setGroupSummary(true)
    }

    override fun handlePushInActivity(activity: Activity) = false
}