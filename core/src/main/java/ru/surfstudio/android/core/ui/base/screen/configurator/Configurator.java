package ru.surfstudio.android.core.ui.base.screen.configurator;

import ru.surfstudio.android.core.ui.HasName;
import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScope;

/**
 * Интерфейс конфигуратора экрана, инкапсулирует всю логику работы с даггером
 */
public interface Configurator extends HasName {
    void run();

    void setPersistentScope(PersistentScope persistentScope);
}
