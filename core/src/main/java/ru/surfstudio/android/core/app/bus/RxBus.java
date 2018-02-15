package ru.surfstudio.android.core.app.bus;


import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import ru.surfstudio.android.dagger.scope.PerActivity;

/**
 * Шина сообшений работающая на Rx. Может истользоваться только в контексте одной активити
 */
@PerActivity
public class RxBus {

    private PublishSubject<Object> internalBus = PublishSubject.create();

    @Inject
    public RxBus() {
        //empty
    }

    public <T> Observable<T> observeEvents(Class<T> eventClass) {
        return internalBus.filter(eventClass::isInstance)
                .map(eventClass::cast);
    }

    public <T> void emitEvent(T event){
        internalBus.onNext(event);
    }
}
