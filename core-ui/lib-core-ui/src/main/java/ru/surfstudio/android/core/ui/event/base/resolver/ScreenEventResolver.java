/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Tuev.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
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

    List<ScreenType> ALL_TYPES = Arrays.asList(ScreenType.ACTIVITY, ScreenType.FRAGMENT, ScreenType.WIDGET);
    List<ScreenType> ACTIVITY_AND_FRAGMENT_TYPES = Arrays.asList(ScreenType.ACTIVITY, ScreenType.FRAGMENT);
    List<ScreenType> ACTIVITY_TYPES = Collections.singletonList(ScreenType.ACTIVITY);
    List<ScreenType> FRAGMENT_TYPES = Collections.singletonList(ScreenType.FRAGMENT);
    List<ScreenType> WIDGET_TYPES = Collections.singletonList(ScreenType.WIDGET);

    R resolve(List<D> delegates, E event);

    Class<D> getDelegateType();

    Class<E> getEventType();

    List<ScreenType> getEventEmitterScreenTypes();
}
