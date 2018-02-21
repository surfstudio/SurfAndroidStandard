package ru.surfstudio.android.notification.ui.notification

import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.notification.interactor.push.BaseNotificationTypeData
import java.util.*

/**
 * Фабрика стратегий обработки пуша по его типу
 */
abstract class AbstractPushHandleStrategyFactory {
    private val KEY = "event"

    /**
     * Переопределяем с необходимым соответствием действий(типа пуша) и стратегий
     */
    abstract val map: HashMap<String, PushHandleStrategy<*>>

    /**
     * Переопределяем для получения стратегии по типу данных пуша(используется при холодном старте)
     */
    abstract fun getByTypeData(typeData: BaseNotificationTypeData<*>): PushHandleStrategy<*>

    fun createByData(data: Map<String, String>): PushHandleStrategy<*>? {
        Logger.d("data : ${data[KEY]}")
        return map[data[KEY] ?: ""].apply {
            this?.typeData?.setDataFromMap(data)
        }
    }
}