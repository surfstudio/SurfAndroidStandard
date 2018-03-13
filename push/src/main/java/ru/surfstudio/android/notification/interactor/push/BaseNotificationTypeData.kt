package ru.surfstudio.android.notification.interactor.push

import java.io.Serializable

/**
 * Тип пуш уведомления с данными
 *
 * Конкретный наследник этого класса используется для извлечения конкретных данных
 */
abstract class BaseNotificationTypeData<T : Serializable> : Serializable {

    var data: T? = null

    /**
     * Извлечение данных из параметров
     *
     * @return извлекает данные из параметров
     */
    abstract fun extractData(map: Map<String, String>): T?

    /**
     * Установка данных из параметров
     */
    fun setDataFromMap(map: Map<String, String>) {
        data = extractData(map)
    }
}