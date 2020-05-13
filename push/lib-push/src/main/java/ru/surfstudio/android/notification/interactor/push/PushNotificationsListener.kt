/*
  Copyright (c) 2018-present, SurfStudio LLC, Fedor Atyakshin.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.notification.interactor.push

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.io.Serializable

/**
 * Слушатель, позволяющий подписаться на данные из пуш-уведомлений различных типов
 */
class PushNotificationsListener {

    private val listeners: MutableList<Listener<*, *>> = mutableListOf()

    /**
     * Подписаться на данные из пуш-уведомления типа [clazz]
     */
    @Suppress("UNCHECKED_CAST")
    fun <Data : Serializable, TypeData : BaseNotificationTypeData<Data>> observePushNotificationsByType(
            clazz: Class<TypeData>
    ): Observable<Data> {
        val existedListener: Listener<*, *>? = listeners.firstOrNull { it.clazz == clazz }
        val resultListener: Listener<*, *> =
                if (existedListener != null) {
                    existedListener
                } else {
                    val newListener: Listener<*, *> = Listener(clazz)
                    listeners.add(newListener)
                    newListener
                }
        return resultListener.getEmitter() as Observable<Data>
    }

    /**
     * Послать событие с данными из полученного пуш-уведомления, если для них есть слушатель
     */
    fun onNewPushNotification(typeData: BaseNotificationTypeData<*>) {
        listeners.firstOrNull { it.clazz.isInstance(typeData) }?.onNewPushNotification(typeData)
    }

    /**
     * Класс, инкапсулирующий логику слушателя пуш-уведомлений одного конкретного типа
     */
    private inner class Listener<Data : Serializable, TypeData : BaseNotificationTypeData<Data>>(
            val clazz: Class<TypeData>
    ) {

        private val emitter: PublishSubject<Data> = PublishSubject.create()

        /**
         * Получить эммитер для подписки на данные из пуш-уведомлений типа [clazz]
         */
        fun getEmitter(): Observable<Data> = emitter.hide()

        /**
         * Послать событие с данными из полученного пуш-уведомления, если они есть
         */
        @Suppress("UNCHECKED_CAST")
        fun onNewPushNotification(typeData: BaseNotificationTypeData<*>) {
            typeData.data?.let { emitter.onNext(it as Data) }
        }
    }
}