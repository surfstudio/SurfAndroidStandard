package ru.surfstudio.android.core.ui.event.base.resolver;


import java.util.ArrayList;
import java.util.List;

import ru.surfstudio.android.core.ui.event.base.ScreenEvent;
import ru.surfstudio.android.core.ui.event.base.ScreenEventDelegate;

/**
 * базовый тип ScreenEventResolver, который позволяет отбработать событие всеми
 * зарегистрированными делегатами соответствующего типа
 *
 * @param <E> событие
 * @param <D> делегат
 * @param <R> возвращаемое значение
 */
public abstract class MultipleScreenEventResolver<E extends ScreenEvent, D extends ScreenEventDelegate, R>
        implements ScreenEventResolver<E, D, List<R>> {

    protected abstract R resolve(D delegate, E event);

    @Override
    public List<R> resolve(List<D> delegates, E event) {
        List<R> result = new ArrayList<>();
        for (D delegate : delegates) {
            result.add(resolve(delegate, event));
        }
        return result;
    }
}
