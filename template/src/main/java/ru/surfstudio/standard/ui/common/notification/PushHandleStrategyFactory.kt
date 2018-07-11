package ru.surfstudio.standard.ui.common.notification

import ru.surfstudio.android.notification.ui.notification.AbstractPushHandleStrategyFactory
import ru.surfstudio.android.notification.ui.notification.strategies.PushHandleStrategy
import ru.surfstudio.standard.ui.common.notification.strategies.NoActionStrategy
import java.util.*

/**
 * Фабрика стратегий обработки пуша по его типу
 */
object PushHandleStrategyFactory : AbstractPushHandleStrategyFactory() {
    override val map: HashMap<String, PushHandleStrategy<*>> = HashMap<String, PushHandleStrategy<*>>()
            .apply {
                put("", NoActionStrategy()) //todo определить
            }
}