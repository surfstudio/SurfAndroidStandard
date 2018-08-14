package ru.surfstudio.android.firebase.sample.ui.common.notification

import ru.surfstudio.android.firebase.sample.ui.common.notification.strategies.simple.NoActionAndDataPushStrategy
import ru.surfstudio.android.firebase.sample.ui.common.notification.strategies.simple.NoDataPushStrategy
import ru.surfstudio.android.firebase.sample.ui.common.notification.strategies.simple.StringPushStrategy
import ru.surfstudio.android.notification.ui.notification.AbstractPushHandleStrategyFactory
import ru.surfstudio.android.notification.ui.notification.strategies.PushHandleStrategy
import java.util.HashMap

/**
 * Фабрика стратегий обработки пуша по его типу
 */
object PushHandleStrategyFactory : AbstractPushHandleStrategyFactory() {
    override val map: HashMap<String, PushHandleStrategy<*>> = HashMap<String, PushHandleStrategy<*>>()
            .apply {
                putAll(mapOf(
                        "" to NoActionAndDataPushStrategy(),
                        "" to NoDataPushStrategy(),
                        "" to StringPushStrategy())
                )
            }
}