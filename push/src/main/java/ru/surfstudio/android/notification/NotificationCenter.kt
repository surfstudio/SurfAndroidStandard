package ru.surfstudio.android.notification

import android.content.Context
import ru.surfstudio.android.core.app.ActiveActivityHolder
import ru.surfstudio.android.notification.interactor.push.PushInteractor
import ru.surfstudio.android.notification.ui.notification.AbstractPushHandleStrategyFactory
import ru.surfstudio.android.notification.ui.notification.PushHandler

/**
 * Помощник инициализации нотификаций
 */
object NotificationCenter {

    private lateinit var activeActivityHolder: ActiveActivityHolder
    private lateinit var pushHandleStrategyFactory: AbstractPushHandleStrategyFactory

    private lateinit var pushHandler: PushHandler

    fun setActiveActivityHolder(activeActivityHolder: ActiveActivityHolder) =
            apply { this.activeActivityHolder = activeActivityHolder }

    fun setPushHandleStrategyFactory(pushHandleStrategyFactory: AbstractPushHandleStrategyFactory) =
            apply { this.pushHandleStrategyFactory = pushHandleStrategyFactory }

    fun configure(conf: NotificationCenter.() -> Unit) {
        this.conf()
        pushHandler = PushHandler(activeActivityHolder, pushHandleStrategyFactory, PushInteractor())
    }

    /**
     * Обработка сообщения из FirebaseMessagingService
     */
    fun onReceiveMessage(context: Context, title: String, body: String, data: Map<String, String>) {
        pushHandler.handleMessage(context, title, body, data)
    }
}