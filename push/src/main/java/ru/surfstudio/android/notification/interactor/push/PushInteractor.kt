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
