package ru.surf.android.notification.interactor.push

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import ru.surfstudio.android.core.app.scheduler.SchedulersProvider
import ru.surfstudio.android.dagger.scope.PerApplication
import javax.inject.Inject

/**
 * Логика работы с пушами
 */
@PerApplication
class PushInteractor @Inject constructor(private val schedulersProvider: SchedulersProvider) {

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
