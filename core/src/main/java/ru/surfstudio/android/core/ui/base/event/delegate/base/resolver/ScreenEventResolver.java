package ru.surfstudio.android.core.ui.base.event.delegate.base.resolver;


import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEvent;
import ru.surfstudio.android.core.ui.base.event.delegate.base.ScreenEventDelegate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface ScreenEventResolver<E extends ScreenEvent, D extends ScreenEventDelegate, R> {

    List<ScreenType> ACTIVITY_AND_FRAGMENT_TYPES = Arrays.asList(ScreenType.ACTIVITY, ScreenType.FRAGMENT);
    List<ScreenType> ACTIVITY_TYPES = Collections.singletonList(ScreenType.ACTIVITY);
    List<ScreenType> FRAGMENT_TYPES = Collections.singletonList(ScreenType.FRAGMENT);

    R resolve(List<D> delegates, E event);
    Class<D> getDelegateType();
    Class<E> getEventType();
    List<ScreenType> getEventEmitterScreenTypes();
}
