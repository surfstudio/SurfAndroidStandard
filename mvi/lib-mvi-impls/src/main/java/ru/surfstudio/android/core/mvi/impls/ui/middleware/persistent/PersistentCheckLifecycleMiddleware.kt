package ru.surfstudio.android.core.mvi.impls.ui.middleware.persistent

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.impls.ui.middleware.dsl.EventTransformerList
import ru.surfstudio.android.core.mvi.impls.ui.middleware.dsl.LifecycleMiddleware
import ru.surfstudio.android.core.ui.scope.ScreenPersistentScope
import ru.surfstudio.android.core.ui.state.ScreenState

/**
 * [LifecycleMiddleware] с поддержкой проверки lifecycle-эвентов через screenPersistentScope.
 */
interface PersistentCheckLifecycleMiddleware<T : Event> : LifecycleMiddleware<T> {

    val screenState: ScreenState

    /**
     * Событие ЖЦ, вызываемое только при первом создании экрана, и игнорируемое при восстановлении экрана
     * после смены конфигурации или убийства процесса.
     *
     * Полезно использовать в том случае, когда нам нужно выполнить событие только один раз (например, добавить фрагмент).
     */
    fun EventTransformerList<T>.onFirstCreate(): Observable<T> =
        eventStream.onCreate().filter { !screenState.isRestoredFromDisk }

    /**
     * Событие ЖЦ, вызываемое при каждом пересоздании экрана.
     *
     * @return Observable, содержащий Boolean-флаг, показывающий, пересоздана ли иерархия view после смены конфигурации, или нет.
     * Если экран был восстановлен с диска, вернет false.
     */
    fun EventTransformerList<T>.onViewCreate(): Observable<Boolean> =
        eventStream.onViewRecreate().map { screenState.isViewRecreated }
}
