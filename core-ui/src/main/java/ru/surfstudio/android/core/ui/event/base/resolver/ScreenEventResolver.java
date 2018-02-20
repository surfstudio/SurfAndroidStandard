package ru.surfstudio.android.core.ui.event.base.resolver;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.event.base.ScreenEvent;
import ru.surfstudio.android.core.ui.event.base.ScreenEventDelegate;

/**
 * определяет связь ScreenEvent c ScreenEventDelegate
 * определяет способ обработки ScreenEvent списком соответствующих делегатов
 *
 * @param <E> событие
 * @param <D> делегат
 * @param <R> возвращаемое значение
 *            <p>
 *            см {@link ScreenEventDelegateManager}
 */
public interface ScreenEventResolver<E extends ScreenEvent, D extends ScreenEventDelegate, R> {

    List<ScreenType> ACTIVITY_AND_FRAGMENT_TYPES = Arrays.asList(ScreenType.ACTIVITY, ScreenType.FRAGMENT);
    List<ScreenType> ACTIVITY_TYPES = Collections.singletonList(ScreenType.ACTIVITY);
    List<ScreenType> FRAGMENT_TYPES = Collections.singletonList(ScreenType.FRAGMENT);

    R resolve(List<D> delegates, E event);

    Class<D> getDelegateType();

    Class<E> getEventType();

    List<ScreenType> getEventEmitterScreenTypes();
}
