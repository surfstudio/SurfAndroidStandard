package ru.surfstudio.android.notification.ui.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

internal const val NOTIFICATION_GROUP_ID = "notification_group_id"

class NotificationDismissedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val groupId = intent.getIntExtra(NOTIFICATION_GROUP_ID, -1)
        NotificationGroupHelper.clearStringDatas(context, groupId)
    }
}