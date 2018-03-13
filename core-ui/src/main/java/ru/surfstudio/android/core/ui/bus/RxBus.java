package ru.surfstudio.android.core.ui.bus;


import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Шина сообшений работающая на Rx. Может истользоваться только в контексте одной активити
 */
public class RxBus {

    private PublishSubject<Object> internalBus = PublishSubject.create();

    public RxBus() {
        //empty
    }

    public <T> Observable<T> observeEvents(Class<T> eventClass) {
        return internalBus.filter(eventClass::isInstance)
                .map(eventClass::cast);
    }

    public <T> void emitEvent(T event) {
        internalBus.onNext(event);
    }
}
