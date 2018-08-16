package ru.surfstudio.android.firebase.sample.ui.common.notification

import ru.surfstudio.android.firebase.sample.domain.notification.NotificationType
import ru.surfstudio.android.firebase.sample.ui.common.notification.strategies.simple.NoDataPushStrategy
import ru.surfstudio.android.firebase.sample.ui.common.notification.strategies.simple.data.FirstDataTypePushStrategy
import ru.surfstudio.android.firebase.sample.ui.common.notification.strategies.simple.data.SecondDataTypePushStrategy
import ru.surfstudio.android.notification.ui.notification.AbstractPushHandleStrategyFactory
import ru.surfstudio.android.notification.ui.notification.strategies.PushHandleStrategy
import java.util.HashMap

/**
 * Фабрика стратегий обработки пуша по его типу
 */
object PushHandleStrategyFactory : AbstractPushHandleStrategyFactory() {
    override val map: HashMap<String, PushHandleStrategy<*>> = hashMapOf(
            NotificationType.FIRST_TYPE.getStringId() to FirstDataTypePushStrategy(),
            NotificationType.SECOND_TYPE.getStringId() to SecondDataTypePushStrategy(),
            NotificationType.NO_DATA_TYPE.getStringId() to NoDataPushStrategy()
    )
}