package ru.surfstudio.android.notification

import android.app.Activity
import android.content.Context
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.notification.interactor.push.IntentPushDataConverter
import ru.surfstudio.android.notification.ui.notification.AbstractPushHandleStrategyFactory
import ru.surfstudio.android.notification.ui.notification.PushHandler
import ru.surfstudio.android.notification.ui.notification.PushHandlingActivity

/**
 * Класс, управляющий перехватом пушей
 */
class NotificationManager {

    private lateinit var pushHandleStrategyFactory: AbstractPushHandleStrategyFactory

    private lateinit var pushHandler: PushHandler

    fun setPushHandleStrategyFactory(pushHandleStrategyFactory: AbstractPushHandleStrategyFactory) =
            apply { this.pushHandleStrategyFactory = pushHandleStrategyFactory }

    fun setPushHandler(pushHandler: PushHandler) =
            apply { this.pushHandler = pushHandler }
    /**
     * Перехват старта активити, помеченной маркерным интерфейсом [PushHandlingActivity]
     * Данный мметод должен быть добавлен в DefaultActivityLifecycleCallbacks
     */
    fun onActivityStarted(activity: Activity) {
        if (activity is PushHandlingActivity) {
            Logger.i("PUSH HANDLE ON $activity")
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

/**
 * DSL для создания менеджера нотификаций
 * @param configure
 */
fun notificationManager(configure: NotificationManager.() -> Unit): NotificationManager {
    val nm = NotificationManager()
    nm.configure()
    return nm
}