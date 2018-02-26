package ru.surfstudio.android.notification

import android.app.Activity
import android.content.Context
import ru.surfstudio.android.core.app.ActiveActivityHolder
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.notification.interactor.push.IntentPushDataConverter
import ru.surfstudio.android.notification.interactor.push.PushInteractor
import ru.surfstudio.android.notification.ui.notification.AbstractPushHandleStrategyFactory
import ru.surfstudio.android.notification.ui.notification.PushHandler
import ru.surfstudio.android.notification.ui.notification.PushHandlingActivity

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
     * Перехват старта активити, помеченной маркерным интерфейсом [PushHandlingActivity]
     * Данный мметод должен быть добавлен в DefaultActivityLifecycleCallbacks
     */
    fun onActivityStarted(activity: Activity) {
        Logger.i("PUSH HANDLE ON $activity")
        if (activity is PushHandlingActivity) {
            val strategy = pushHandleStrategyFactory.createByData(IntentPushDataConverter.convert(activity.intent))
            if (strategy != null) activity.startActivity(strategy.coldStartRoute().prepareIntent(activity))
        }
    }

    /**
     * Обработка сообщения из FirebaseMessagingService
     */
    fun onReceiveMessage(context: Context, title: String, body: String, data: Map<String, String>) {
        pushHandler.handleMessage(context, title, body, data)
    }
}