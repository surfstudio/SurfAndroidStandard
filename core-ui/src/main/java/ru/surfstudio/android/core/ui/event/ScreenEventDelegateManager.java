package ru.surfstudio.android.core.ui.event;


import android.support.annotation.Nullable;

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
     * удаляет подписку делегата
     *
     * @return была ли подписка удалена
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
}
