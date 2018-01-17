package ru.surfstudio.android.core.ui.base.event.delegate.base.resolver;


import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEventDelegate;

import java.util.ArrayList;
import java.util.List;

public abstract class MultipleScreenEventResolver<E extends ScreenEvent, D extends ScreenEventDelegate, R>
        implements ScreenEventResolver<E, D, List<R>> {

    protected abstract R resolve(D delegate, E event);

    @Override
    public List<R> resolve(List<D> delegates, E event){
        List<R> result = new ArrayList<>();
        for (D delegate : delegates) {
            result.add(resolve(delegate, event));
        }
        return result;
    }
}
