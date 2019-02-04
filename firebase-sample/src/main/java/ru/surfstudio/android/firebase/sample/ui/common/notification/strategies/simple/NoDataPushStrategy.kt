package ru.surfstudio.android.firebase.sample.ui.common.notification.strategies.simple

import android.content.Context
import ru.surfstudio.android.firebase.sample.R
import ru.surfstudio.android.firebase.sample.ui.common.notification.strategies.type.NoDataNotificationTypeData
import ru.surfstudio.android.firebase.sample.ui.screen.push.PushActivityRoute

/**
 * Стратегия для пушей без данных
 */
class NoDataPushStrategy : BaseSimplePushStrategy<NoDataNotificationTypeData>() {
    override val typeData by lazy { NoDataNotificationTypeData() }

    override val channelName: Int = R.string.no_data_push_channel_name

    override fun coldStartIntent(context: Context) = PushActivityRoute().prepareIntent(context)
}