package ru.surfstudio.android.notification

import android.content.Context
import ru.surfstudio.android.notification.ui.notification.AbstractPushHandleStrategyFactory
import ru.surfstudio.android.core.util.ActiveActivityHolder

/**
 * Помощник инициализации нотификаций
 */
object NotificationCenter {

    lateinit var activeActivityHolder: ActiveActivityHolder
    lateinit var pushHandleStrategyFactory: AbstractPushHandleStrategyFactory

    fun configure( conf: NotificationCenter.() -> Unit) {
        this.apply(conf)
    }

    val notificationComponent: NotificationComponent by lazy {
        DaggerNotificationComponent.builder()
                .notificationModule(NotificationModule(activeActivityHolder, pushHandleStrategyFactory))
                .build()
    }

    /**
     * Обработка сообщения из FirebaseMessagingService
     */
    fun onReceiveMessage(context: Context, title: String, body: String, data: Map<String, String>) {
        notificationComponent.pushHandler().handleMessage(context, title, body, data)
    }
}