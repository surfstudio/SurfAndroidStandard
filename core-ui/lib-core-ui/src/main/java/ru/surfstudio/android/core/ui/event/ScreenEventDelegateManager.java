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
package ru.surfstudio.android.core.ui.event;


import androidx.annotation.Nullable;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.event.base.ScreenEvent;
import ru.surfstudio.android.core.ui.event.base.ScreenEventDelegate;
import ru.surfstudio.android.core.ui.event.base.resolver.ScreenEventResolver;

/**
 * менеджер делегатов для событий экранв
 * см {@link ScreenEvent}, {@link ScreenEventDelegate}, {@link ScreenEventResolver}
 */
public interface ScreenEventDelegateManager {
    /**
     * регистрирует делегат
     */
    void registerDelegate(ScreenEventDelegate delegate);

    /**
     * регистрирует делегат, причем позволяет зарегистриеровать его
     * на событие конкретного типа экрана
     * Если emitterType не указан, то регистрируется в ближайшем подходящем
     * ScreenEventDelegateManager в иерархии экранов
     * Например, если ScreenEventDelegateManager отвечает за управление событиями фрагмента,
     * то при подписки на событие onActivityResult при указании ScreenType.ACTIVITY в качестве emitterType,
     * делегат подпишется на событие из активити, а не из фрагмента
     */
    void registerDelegate(ScreenEventDelegate delegate, @Nullable ScreenType emitterType);

    /**
     * Ренистрирует делегата на конкретный эвент
     *
     * @param delegate
     * @param eventType
     */
    void registerDelegate(ScreenEventDelegate delegate,
                          @Nullable ScreenType emitterType,
                          Class<? extends ScreenEvent> eventType);

    /**
     * удаляет подписку делегата по событию
     *
     * @return была ли подписка удалена
     */
    <E extends ScreenEvent> boolean unregisterDelegate(ScreenEventDelegate delegate, Class<E> event);

    /**
     * Отписывает подписку делегата
     *
     * @param delegate
     * @return
     */
    boolean unregisterDelegate(ScreenEventDelegate delegate);

    /**
     * отправляет событие
     * Используется во внутренней логике экрана
     *
     * @param <E> сбытие
     * @param <D> делегат
     * @param <R> возвращаемый результат
     * @return результат обработки события
     */
    <E extends ScreenEvent, D extends ScreenEventDelegate, R> R sendEvent(E event);

    /**
     * отменяет все подписки, после вызова этого метода менеджером пользоваться больше нельзя
     */
    void destroy();

    /**
     * Рассылает сообщения из хранилища
     */
    void sendUnhandledEvents();
}
