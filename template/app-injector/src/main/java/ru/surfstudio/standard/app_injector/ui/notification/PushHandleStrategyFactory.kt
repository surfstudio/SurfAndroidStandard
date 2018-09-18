package ru.surfstudio.standard.app_injector.ui.notification

import ru.surfstudio.android.notification.ui.notification.AbstractPushHandleStrategyFactory
import ru.surfstudio.android.notification.ui.notification.strategies.PushHandleStrategy
import ru.surfstudio.standard.app_injector.ui.notification.strategies.DebugPushHandleStrategy
import ru.surfstudio.standard.domain.notification.NotificationType
import java.util.HashMap

/**
 * Фабрика стратегий обработки пуша по его типу
 */
object PushHandleStrategyFactory : AbstractPushHandleStrategyFactory() {
    override val map: HashMap<String, PushHandleStrategy<*>> = hashMapOf(
            NotificationType.DEBUG_DATA_TYPE.getStringId() to DebugPushHandleStrategy()
    )
}