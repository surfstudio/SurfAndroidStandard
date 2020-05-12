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

/**
 * Логика работы с пушами
 */
class PushInteractor {

    private val notificationPublishSubject = PublishSubject.create<BaseNotificationTypeData<*>>()

    /**
     * Подписка на пуши определенного типа.
     * Нужно подписываться, когда на экране нужно сделать определенное действие
     * по данным пуша определенного типа.
     */
    fun <T> observeNotificationType(eventClass: Class<T>): Observable<T> {
        return notificationPublishSubject.filter { b -> eventClass.isInstance(b) }.map { eventClass.cast(it) }
    }

    /**
     * Посылает событие по пушу определенного типа с данными
     */
    fun onNewNotification(data: BaseNotificationTypeData<*>) {
        notificationPublishSubject.onNext(data)
    }
}
