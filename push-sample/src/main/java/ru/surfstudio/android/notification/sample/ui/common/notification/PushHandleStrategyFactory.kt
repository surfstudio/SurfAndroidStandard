package ru.surfstudio.android.notification.sample.ui.common.notification

import ru.surfstudio.android.notification.ui.notification.AbstractPushHandleStrategyFactory
import ru.surfstudio.android.notification.ui.notification.strategies.PushHandleStrategy
import java.util.HashMap

/**
 * Фабрика стратегий обработки пуша по его типу
 */
object PushHandleStrategyFactory : AbstractPushHandleStrategyFactory() {
    override val map: HashMap<String, PushHandleStrategy<*>> = HashMap<String, PushHandleStrategy<*>>()
            .apply {
                //put("", NoActionStrategy())
            }
}