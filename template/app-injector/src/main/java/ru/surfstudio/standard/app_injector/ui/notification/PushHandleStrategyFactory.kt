package ru.surfstudio.standard.app_injector.ui.notification

import ru.surfstudio.android.notification.ui.notification.strategies.PushHandleStrategy
import ru.surfstudio.android.notification.ui.notification.strategies.storage.AbstractPushHandleStrategyFactory
import java.util.*

/**
 * Фабрика стратегий обработки пуша по его типу
 */
object PushHandleStrategyFactory : AbstractPushHandleStrategyFactory() {
    override val map: HashMap<String, PushHandleStrategy<*>> = hashMapOf()
}