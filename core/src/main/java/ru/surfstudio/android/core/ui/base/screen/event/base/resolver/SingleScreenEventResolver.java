package ru.surfstudio.android.core.ui.base.screen.event.base.resolver;


import android.util.Log;

import java.util.List;

import ru.surfstudio.android.core.ui.base.screen.event.base.ScreenEvent;
import ru.surfstudio.android.core.ui.base.screen.event.base.ScreenEventDelegate;

/**
 * базовый тип ScreenEventResolver, который позволяет отбработать событие только одним делегатом
 * из всех зарегистрированных соответствующего типа
 * @param <E> событие
 * @param <D> делегат
 */
public abstract class SingleScreenEventResolver<E extends ScreenEvent, D extends ScreenEventDelegate>
        implements ScreenEventResolver<E, D, Boolean> {

    protected abstract boolean resolve(D delegate, E event);

    @Override
    public Boolean resolve(List<D> delegates, E event){
        if(delegates.isEmpty()){
            return false;
        }
        D someDelegate = delegates.get(0);
        for (D delegate : delegates) {
            if (resolve(delegate, event)) {
                return true;
            }
        }
        logUnhandled(someDelegate.getClass(), event);
        return false;
    }

    private void logUnhandled(Class delegateClass, ScreenEvent event) {
        String tag = this.getClass().getSimpleName();
        Log.i(tag, String.format("Unhandled screen event: %s "
                        + "\nProbably you do not register delegate %s "
                        + "\nor you handle this event on fragment",
                event.toString(), delegateClass.getSimpleName()));
    }
}
