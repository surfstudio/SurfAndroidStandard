package ru.surfstudio.android.core.mvp.configurator

import ru.surfstudio.android.core.mvp.view.PresenterHolderCoreView

/**
 * Компонент для экранов с биндингом.
 * Вызов [requestInjection] проинциализирует все зависимости c типом [Any] в даггер модуле.
 * Пример: используется для инициализации презентера, без явного указания @Inject на поле.
 */
interface BindableScreenComponent<V : PresenterHolderCoreView> : ScreenComponent<V> {

    fun requestInjection(): Any
}
