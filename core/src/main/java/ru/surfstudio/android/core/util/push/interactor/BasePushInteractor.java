package ru.surfstudio.android.core.util.push.interactor;


import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Логика работы с пушами
 */
public class BasePushInteractor {

    private final PublishSubject<BaseAbstractNotificationTypeData> notificationPublishSubject = PublishSubject.create();

    /**
     * Подписка на пуши определенного типа.
     * Нужно подписываться, когда на экране нужно сделать определенное действия
     * по данным пуша определенного типа.
     */
    public <T> Observable<T> observeNotificationType(Class<T> eventClass) {
        return notificationPublishSubject.filter(eventClass::isInstance).map(eventClass::cast);
    }

    /**
     * Посылает собыитие по пушу определенного типа с данными
     */
    public void onNewNotification(BaseAbstractNotificationTypeData data) {
        notificationPublishSubject.onNext(data);
    }
}
