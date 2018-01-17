package ru.surfstudio.android.core.ui.base.event.delegate.base.resolver;


import android.util.Log;

import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEventDelegate;

import java.util.List;

public abstract class SingleScreenEventResolver<E extends ScreenEvent, D extends ScreenEventDelegate>
        implements ru.surfstudio.android.core.ui.base.event.delegate.base.resolver.ScreenEventResolver<E, D, Boolean> {

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
        Log.e(tag, String.format("Unhandled screen event: %s "
                        + "\nProbably you do not register delegate %s "
                        + "\nor you handle this event on fragment",
                event.toString(), delegateClass.getSimpleName()));
    }
}
