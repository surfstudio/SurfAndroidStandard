package ru.surf.android.notification.interactor.push

import java.io.Serializable

/**
 * Тип пуш уведомления с данными
 *
 * Конкретный наследний этого класса используется для определения необходимого действия
 */
abstract class BaseNotificationTypeData<T : Serializable> : Serializable {

    var data: T? = null

    /**
     * Действие по нажатию на пуш при холодном старте приложения
     */
    abstract fun performCoolStartAction(actions: () -> Unit)

    /**
     * Извлечение данных из параметров
     *
     * @return извлекает данные из параметров
     */
    abstract fun extractData(map: Map<String, String>): T

    /**
     * Установка данных из параметров
     */
    fun setDataFromMap(map: Map<String, String>) {
        data = extractData(map)
    }
}